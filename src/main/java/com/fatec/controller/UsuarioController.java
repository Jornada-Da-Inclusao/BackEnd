// Define o pacote onde o controlador está localizado.
package com.fatec.controller;

// Importações necessárias para manipulação de dados e requisições HTTP
import java.util.List;
import java.util.Optional;

import com.fatec.dto.UsuarioUpdateDTO;
import com.fatec.model.Jogos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fatec.model.Usuario;
import com.fatec.model.UsuarioLogin;
import com.fatec.repository.UsuarioRepository;
import com.fatec.service.UsuarioService;

import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

// Anotação que indica que esta classe será um controlador REST no Spring.
@RestController
// Define o caminho base para as requisições de endpoints dessa classe.
@RequestMapping("/usuarios")
// Permite requisições de qualquer origem (CORS), ou seja, qualquer domínio pode consumir os endpoints dessa API.
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

	// O Spring vai automaticamente injetar as dependências necessárias.
	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	// Endpoint para buscar um usuário pelo ID
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> getById(@PathVariable Long id) {
		// Tenta encontrar o usuário no banco de dados.
		return usuarioRepository.findById(id)
				// Se o usuário for encontrado, retorna com o status 200 OK e o usuário.
				.map(resposta -> ResponseEntity.ok(resposta))
				// Caso contrário, retorna 404 NOT FOUND.
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	// Endpoint para cadastrar um novo usuário e enviar um código de verificação por e-mail
	@PostMapping("/cadastrar")
	public ResponseEntity<Usuario> postUsuario(@Valid @RequestBody Usuario usuario){
		// Chama o serviço para cadastrar o usuário e retorna a resposta apropriada
		return usuarioService.cadastrarUsuario(usuario)
				.map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta))  // Se sucesso, retorna 201 Created
				.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());  // Caso haja erro, retorna 400 Bad Request
	}

	// A anotação @Value injeta o valor do nome da fila de e-mails configurada no arquivo de propriedades.
	@Value("${broker.queue.email.name}")
	private String queueEmail;

	// Endpoint para testar a comunicação com o RabbitMQ.
	@GetMapping("/test-rabbit")
	public String testRabbit() {
		// Envia uma mensagem de teste para a fila RabbitMQ.
		rabbitTemplate.convertAndSend(queueEmail, "Mensagem de teste!");
		return "Mensagem enviada!";
	}

	// Endpoint para atualizar completamente os dados de um usuário
	@PutMapping("/atualizar")
	public ResponseEntity<Usuario> putUsuario(@Valid @RequestBody Usuario usuario) {
		// Chama o serviço que realiza a atualização completa dos dados do usuário
		return usuarioService.atualizarUsuario(usuario)
				// Se a atualização for bem-sucedida, retorna o status 200 OK com o usuário atualizado.
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
				// Caso o usuário não exista, retorna o status 404 NOT FOUND.
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	// Endpoint para atualização parcial dos dados de um usuário
	@PatchMapping("/atualizar-parcial")
	public ResponseEntity<Usuario> patchUsuario(@RequestBody UsuarioUpdateDTO dto) {
		// Chama o serviço que realiza a atualização parcial com os dados fornecidos no DTO.
		return usuarioService.atualizarParcial(dto)
				// Se a atualização for bem-sucedida, retorna o status 200 OK com o usuário atualizado.
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
				// Caso o usuário não exista, retorna o status 404 NOT FOUND.
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	// Endpoint para autenticação do usuário, recebendo credenciais para login
	@PostMapping("/logar")
	public ResponseEntity<UsuarioLogin> autenticarUsuario(@Valid @RequestBody Optional<UsuarioLogin> usuarioLogin) {
		// Chama o serviço de autenticação, passando as credenciais do usuário.
		return usuarioService.autenticarUsuario(usuarioLogin)
				// Se a autenticação for bem-sucedida, retorna o token com status 200 OK.
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
				// Se falhar, retorna status 401 UNAUTHORIZED.
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}

	// Endpoint para excluir um usuário pelo ID
	@ResponseStatus(HttpStatus.NO_CONTENT)  // Indica que não há conteúdo a ser retornado, mas a exclusão foi bem-sucedida.
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		// Tenta encontrar o usuário pelo ID no banco de dados.
		Optional<Usuario> usuario = usuarioRepository.findById(id);

		// Se o usuário não for encontrado, lança uma exceção com o status 404 NOT FOUND.
		if(usuario.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		// Caso o usuário exista, deleta o registro no banco de dados.
		usuarioRepository.deleteById(id);
	}
}
