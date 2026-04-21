package com.fatec.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fatec.model.Usuario;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Long id; // 🔥 novo: identificador único do usuário
	private String userName;
	private String password;
	private List<GrantedAuthority> authorities;

	// =========================
	// Construtor principal
	// =========================
	public UserDetailsImpl(Usuario usuario) {
		this.id = usuario.getId(); // 🔥 agora usamos o ID
		this.userName = usuario.getEmail(); // pode continuar sendo email como username
		this.password = usuario.getSenha();
		this.authorities = List.of(); // ou carregar roles depois
	}

	public UserDetailsImpl() {}

	// =========================
	// 🔥 Novo getter importante
	// =========================
	public Long getId() {
		return id;
	}

	// =========================
	// Spring Security methods
	// =========================

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}