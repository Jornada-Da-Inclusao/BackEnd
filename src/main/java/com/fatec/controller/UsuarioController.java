package com.fatec.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.model.Usuario;
import com.fatec.model.UsuarioLogin;
import com.fatec.repository.UsuarioRepository;
import com.fatec.service.UsuarioService;

import jakarta.validation.Valid;

@RestController  // Anotação que define esta classe como um controlador REST
@RequestMapping("/usuarios")  // Define o caminho base para as requisições dessa classe
@CrossOrigin(origins = "*", allowedHeaders = "*")  // Permite requisições de qualquer origem e com quaisquer cabeçalhos
public class UsuarioController {

	@Autowired  // Injeta as dependências automaticamente
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	// Método para obter todos os usuários cadastrados
	@GetMapping("/all")
	public ResponseEntity<List<Usuario>> getAll(){
		// Retorna todos os usuários encontrados no repositório
		return ResponseEntity.ok(usuarioRepository.findAll());
	}

	// Método para buscar um usuário pelo ID
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> getById(@PathVariable Long id) {
		// Verifica se o usuário existe, caso contrário, retorna "Not Found"
		return usuarioRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))  // Se encontrado, retorna OK com os dados do usuário
				.orElse(ResponseEntity.notFound().build());  // Caso não encontrado, retorna 404 Not Found
	}

	// Método para cadastrar um novo usuário
	@PostMapping("/cadastrar")
	public ResponseEntity<Usuario> postUsuario(@Valid @RequestBody Usuario usuario){
		// Chama o serviço para cadastrar o usuário e retorna a resposta apropriada
		return usuarioService.cadastrarUsuario(usuario)
				.map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta))  // Se sucesso, retorna 201 Created
				.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());  // Caso haja erro, retorna 400 Bad Request
	}

	// Método para atualizar os dados de um usuário
	@PutMapping("/atualizar")
	public ResponseEntity<Usuario> putUsuario(@Valid @RequestBody Usuario usuario){
		// Chama o serviço para atualizar o usuário e retorna a resposta apropriada
		return usuarioService.atualizarUsuario(usuario)
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))  // Se sucesso, retorna 200 OK
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());  // Caso o usuário não exista, retorna 404 Not Found
	}

	// Método para autenticar um usuário
	@PostMapping("/logar")
	public ResponseEntity<UsuarioLogin> autenticarUsuario(@Valid @RequestBody Optional<UsuarioLogin> usuarioLogin){
		// Chama o serviço de autenticação e retorna a resposta apropriada
		return usuarioService.autenticarUsuario(usuarioLogin)
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))  // Se sucesso, retorna 200 OK com o token
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());  // Caso falhe, retorna 401 Unauthorized
	}

}
