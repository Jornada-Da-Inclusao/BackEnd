package com.fatec.service;

import java.util.Optional;
import java.util.UUID;

import com.fatec.dto.UsuarioUpdateDTO;
import com.fatec.model.EmailVerify;
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

	@Transactional
	public Optional<Usuario> cadastrarUsuarioComCodigo(Usuario usuario) {
		// Verifica se o usuário já existe no banco de dados
		if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) {
			return Optional.empty();  // Retorna vazio caso o nome de usuário já exista
		}

		// Gera um código de verificação único
		String codigoVerificacao = UUID.randomUUID().toString();

		// Atribui o código ao usuário
		usuario.setCodigoVerificacao(codigoVerificacao);
		usuario.setCodigoValidado(false);  // O código ainda não foi validado

		// Envia o código para o e-mail do usuário
		EmailVerify email = new EmailVerify();
		email.setEmailTo(usuario.getUsuario());
		email.setSubject("Código de Verificação");
		email.setText("Seu código de verificação é: " + codigoVerificacao);

		try {
			emailService.sendEmail(email);  // Envia o e-mail com o código de verificação
		} catch (MailException e) {
			return Optional.empty();  // Se não for possível enviar o e-mail, retorna vazio
		}

		// Não salva o usuário completamente ainda
		return Optional.of(usuarioRepository.save(usuario));  // Salva parcialmente
	}

	// Método para validar o código de verificação
	@Transactional
	public Optional<Usuario> validarCodigoVerificacao(String codigoVerificacao) {
		// Busca o usuário pelo código de verificação
		Optional<Usuario> usuarioOpt = usuarioRepository.findByCodigoVerificacao(codigoVerificacao);

		if (usuarioOpt.isPresent()) {
			Usuario usuario = usuarioOpt.get();

			// Marca o código como validado
			usuario.setCodigoValidado(true);
			usuario.setSenha(criptografarSenha(usuario.getSenha()));  // Criptografa a senha antes de salvar

			// Salva o usuário com a senha criptografada e o código validado
			usuarioRepository.save(usuario);

			// Envia o e-mail informando que a conta foi criada com sucesso
			EmailVerify email = new EmailVerify();
			email.setEmailTo(usuario.getUsuario());
			email.setSubject("Conta Criada com Sucesso");
			email.setText("Sua conta foi criada com sucesso!");

			emailService.sendEmail(email);  // Envia o e-mail de confirmação

			return Optional.of(usuario);  // Retorna o usuário completamente cadastrado
		}

		return Optional.empty();  // Se o código não for válido
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
