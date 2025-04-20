package com.fatec.controller;

import java.util.List;
import java.util.Optional;

import com.fatec.dto.UsuarioUpdateDTO;
import com.fatec.model.Jogos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fatec.model.Usuario;
import com.fatec.model.UsuarioLogin;
import com.fatec.repository.UsuarioRepository;
import com.fatec.service.UsuarioService;

import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

@RestController  // Anotação que define esta classe como um controlador REST
@RequestMapping("/usuarios")  // Define o caminho base para as requisições dessa classe
@CrossOrigin(origins = "*", allowedHeaders = "*")  // Permite requisições de qualquer origem e com quaisquer cabeçalhos
public class UsuarioController {

	@Autowired  // Injeta as dependências automaticamente
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@GetMapping("/{id}")
	public ResponseEntity<Usuario> getById(@PathVariable Long id) {
		return usuarioRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
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

		@PatchMapping("/atualizar-parcial")
	public ResponseEntity<Usuario> patchUsuario(@RequestBody UsuarioUpdateDTO dto) {
		return usuarioService.atualizarParcial(dto)
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}


	// Método para autenticar um usuário
	@PostMapping("/logar")
	public ResponseEntity<UsuarioLogin> autenticarUsuario(@Valid @RequestBody Optional<UsuarioLogin> usuarioLogin){
		// Chama o serviço de autenticação e retorna a resposta apropriada
		return usuarioService.autenticarUsuario(usuarioLogin)
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))  // Se sucesso, retorna 200 OK com o token
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());  // Caso falhe, retorna 401 Unauthorized
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);

		if(usuario.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		usuarioRepository.deleteById(id);
	}

}
