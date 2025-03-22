package com.fatec.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fatec.model.Usuario;
import com.fatec.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	// Injeção de dependência do repositório de usuários
	@Autowired
	private UsuarioRepository usuarioRepository;

	// Implementação do método loadUserByUsername da interface UserDetailsService
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		// Procura o usuário no banco de dados pelo nome de usuário
		Optional<Usuario> usuario = usuarioRepository.findByUsuario(userName);

		// Se o usuário for encontrado, retorna um objeto UserDetails com as informações do usuário
		if (usuario.isPresent())
			return new UserDetailsImpl(usuario.get());
		else
			// Se o usuário não for encontrado, lança uma exceção ResponseStatusException com o código de status FORBIDDEN
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
	}
}
