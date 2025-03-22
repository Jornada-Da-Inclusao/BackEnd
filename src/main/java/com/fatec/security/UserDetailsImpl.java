package com.fatec.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fatec.model.Usuario;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private String userName; // Nome de usuário do usuário (identificador único)
	private String password; // Senha do usuário
	private List<GrantedAuthority> authorities; // Lista de permissões/roles do usuário

	// Construtor que inicializa o UserDetailsImpl a partir de um objeto Usuario
	public UserDetailsImpl(Usuario usuario) {
		this.userName = usuario.getUsuario(); // Define o nome de usuário
		this.password = usuario.getSenha(); // Define a senha
		// A lista de authorities (permissões) pode ser configurada conforme necessário, mas está ausente nesse construtor
	}

	// Construtor vazio
	public UserDetailsImpl() { }

	// Retorna a lista de authorities (permissões ou roles) associadas a este usuário
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities; // No código atual, a lista de authorities é nula, mas ela poderia ser configurada em outros métodos
	}

	// Retorna a senha do usuário
	@Override
	public String getPassword() {
		return password; // Retorna a senha armazenada
	}

	// Retorna o nome de usuário (identificador único)
	@Override
	public String getUsername() {
		return userName; // Retorna o nome de usuário
	}

	// Indica se a conta do usuário expirou
	@Override
	public boolean isAccountNonExpired() {
		return true; // Retorna true, indicando que a conta não expirou. Isso pode ser ajustado conforme necessário
	}

	// Indica se a conta do usuário está bloqueada
	@Override
	public boolean isAccountNonLocked() {
		return true; // Retorna true, indicando que a conta não está bloqueada
	}

	// Indica se as credenciais (senha) do usuário expiraram
	@Override
	public boolean isCredentialsNonExpired() {
		return true; // Retorna true, indicando que as credenciais não expiraram
	}

	// Indica se a conta do usuário está habilitada
	@Override
	public boolean isEnabled() {
		return true; // Retorna true, indicando que a conta está habilitada
	}
}
