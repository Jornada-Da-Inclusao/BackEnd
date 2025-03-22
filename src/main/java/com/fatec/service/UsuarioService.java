package com.fatec.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

	@Autowired // Injeção de dependência para o repositório de usuários
	private UsuarioRepository usuarioRepository;

	@Autowired // Injeção de dependência para o serviço de geração de tokens JWT
	private JwtService jwtService;

	@Autowired // Injeção de dependência para o gerenciador de autenticação
	private AuthenticationManager authenticationManager;

	// Método para cadastrar um novo usuário
	public Optional<Usuario> cadastrarUsuario(Usuario usuario) {
		// Verifica se o usuário já existe no banco de dados
		if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())
			return Optional.empty(); // Retorna vazio caso o nome de usuário já exista

		// Se a foto não foi fornecida, atribui uma imagem padrão
		if (usuario.getFoto().isBlank())
			usuario.setFoto("https://i.imgur.com/Zz4rzVR.png");

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

			// Se a foto não foi fornecida, atribui uma imagem padrão
			if (usuario.getFoto().isBlank())
				usuario.setFoto("https://i.imgur.com/Zz4rzVR.png");

			// Criptografa a nova senha
			usuario.setSenha(criptografarSenha(usuario.getSenha()));

			// Atualiza as informações do usuário no banco de dados e retorna a entidade atualizada
			return Optional.ofNullable(usuarioRepository.save(usuario));
		}

		// Retorna vazio caso o usuário não seja encontrado
		return Optional.empty();
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
				usuarioLogin.get().setFoto(usuario.get().getFoto());
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
