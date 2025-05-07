package com.fatec.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.fatec.dto.EmailRecordDto;
import com.fatec.dto.NovaSenhaDTO;
import com.fatec.dto.UsuarioUpdateDTO;
import com.fatec.model.EmailVerify;
import com.fatec.repository.EmailRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fatec.model.Usuario;
import com.fatec.model.UsuarioLogin;
import com.fatec.repository.UsuarioRepository;
import com.fatec.security.JwtService;

@Service // Anotação que marca essa classe como um serviço que será gerenciado pelo Spring
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;
	private final EmailService emailService;

	@Autowired
	public UsuarioService(UsuarioRepository usuarioRepository, EmailService emailService) {
		this.usuarioRepository = usuarioRepository;
		this.emailService = emailService;
	}

	@Autowired // Injeção de dependência para o serviço de geração de tokens JWT
	private JwtService jwtService;

	@Autowired // Injeção de dependência para o gerenciador de autenticação
	private AuthenticationManager authenticationManager;

	@Autowired
	private EmailRepository emailRepository ;

	public Optional<Usuario> cadastrarUsuario(Usuario usuario) {
		// Verifica se o usuário já existe no banco de dados
		if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())
			return Optional.empty(); // Retorna vazio caso o nome de usuário já exista

		// Criptografa a senha do usuário antes de salvar
		usuario.setSenha(criptografarSenha(usuario.getSenha()));

		// Salva o usuário no banco de dados e retorna a entidade salva
		return Optional.of(usuarioRepository.save(usuario));
	}


	// Método para atualizar as informações de um usuário existente
	public Optional<Usuario> atualizarUsuario(Usuario usuario) {
		// Verifica se o usuário existe no banco de dados pelo ID
		if (usuarioRepository.findById(usuario.getId()).isPresent()) {

			// Verifica se o nome de usuário já está sendo utilizado por outro usuário
			Optional<Usuario> buscaUsuario = usuarioRepository.findByUsuario(usuario.getUsuario());
			if ((buscaUsuario.isPresent()) && (buscaUsuario.get().getId() != usuario.getId()))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null); // Lança exceção se o usuário já existir

			// Criptografa a nova senha
			usuario.setSenha(criptografarSenha(usuario.getSenha()));

			// Atualiza as informações do usuário no banco de dados e retorna a entidade atualizada
			return Optional.ofNullable(usuarioRepository.save(usuario));
		}

		// Retorna vazio caso o usuário não seja encontrado
		return Optional.empty();
	}

	public Optional<Usuario> atualizarParcial(UsuarioUpdateDTO dto) {
		Optional<Usuario> usuarioOptional = usuarioRepository.findById(dto.getId());

		if (usuarioOptional.isEmpty())
			return Optional.empty();

		Usuario usuarioExistente = usuarioOptional.get();

		// Verifica se está tentando mudar o email
		if (dto.getUsuario() != null && !dto.getUsuario().equals(usuarioExistente.getUsuario())) {
			Optional<Usuario> buscaUsuario = usuarioRepository.findByUsuario(dto.getUsuario());
			if (buscaUsuario.isPresent() && buscaUsuario.get().getId() != usuarioExistente.getId()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);
			}
			usuarioExistente.setUsuario(dto.getUsuario().toLowerCase()); // já normaliza aqui
		}

		if (dto.getNome() != null)
			usuarioExistente.setNome(dto.getNome());

		if (dto.getSenha() != null)
			usuarioExistente.setSenha(criptografarSenha(dto.getSenha()));

		return Optional.of(usuarioRepository.save(usuarioExistente));
	}


	// Método para autenticar um usuário
	public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) {
		// Cria um objeto de autenticação com o nome de usuário e senha fornecidos
		var credenciais = new UsernamePasswordAuthenticationToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha());

		// Tenta autenticar o usuário com as credenciais fornecidas
		Authentication authentication = authenticationManager.authenticate(credenciais);

		// Se a autenticação for bem-sucedida
		if (authentication.isAuthenticated()) {

			// Busca o usuário no banco de dados pelo nome de usuário
			Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());

			// Se o usuário for encontrado
			if (usuario.isPresent()) {
				// Preenche o objeto UsuarioLogin com os dados do usuário encontrado
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setToken(gerarToken(usuarioLogin.get().getUsuario())); // Gera o token JWT para o usuário
				usuarioLogin.get().setSenha(""); // Limpa a senha do objeto de login antes de retorná-lo

				// Retorna o objeto UsuarioLogin preenchido com as informações do usuário
				return usuarioLogin;
			}
		}

		// Retorna vazio se a autenticação falhar ou se o usuário não for encontrado
		return Optional.empty();
	}

	public boolean atualizarSenhaViaToken(NovaSenhaDTO dto) {
		Optional<EmailVerify> tokenOptional = emailRepository.findByToken(dto.getToken());

		if (tokenOptional.isEmpty()) return false;

		EmailVerify tokenEntity = tokenOptional.get();

		// Verifica se o token está expirado ou já foi usado
		if (tokenEntity.getExp().isBefore(LocalDateTime.now()) || tokenEntity.isStatus()) {
			return false;
		}

		// Busca o usuário pelo e-mail associado ao token
		Optional<Usuario> usuarioOptional = usuarioRepository.findByUsuario(tokenEntity.getUserEmail());

		if (usuarioOptional.isEmpty()) return false;

		Usuario usuario = usuarioOptional.get();

		String senhaCriptografada = criptografarSenha(dto.getNovaSenha());
		usuario.setSenha(senhaCriptografada);

		usuarioRepository.save(usuario);

		// Marcar o token como usado
		tokenEntity.setStatus(true);
		emailRepository.save(tokenEntity);

		return true;
	}

	// Método auxiliar para criptografar a senha usando BCrypt
	private String criptografarSenha(String senha) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // Cria um codificador BCrypt
		return encoder.encode(senha); // Criptografa a senha
	}

	// Método auxiliar para gerar um token JWT para o usuário autenticado
	private String gerarToken(String usuario) {
		return "Bearer " + jwtService.generateToken(usuario); // Gera o token JWT e adiciona o prefixo "Bearer"
	}
}
