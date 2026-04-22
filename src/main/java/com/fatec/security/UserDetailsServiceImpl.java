package com.fatec.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fatec.model.Usuario;
import com.fatec.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	// =========================
	// Login tradicional (email)
	// =========================
	@Override
	public UserDetails loadUserByUsername(String email) {

		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() ->
						new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não encontrado")
				);

		return new UserDetailsImpl(usuario);
	}

	// =========================
	// 🔥 NOVO: autenticação via ID (JWT)
	// =========================
	public UserDetails loadUserById(Long id) {

		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() ->
						new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não encontrado")
				);

		return new UserDetailsImpl(usuario);
	}
}