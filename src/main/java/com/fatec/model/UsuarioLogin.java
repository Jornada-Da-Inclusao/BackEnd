package com.fatec.model;

public class UsuarioLogin {

	// Atributos que representam os dados do usuário que está realizando o login
	private Long id;
	private String nome;
	private String usuario;
	private String senha;
	private String foto;
	private String token;

	// Método que retorna o ID do usuário
	public Long getId() {
		return this.id;
	}

	// Método que define o ID do usuário
	public void setId(Long id) {
		this.id = id;
	}

	// Método que retorna o nome do usuário
	public String getNome() {
		return this.nome;
	}

	// Método que define o nome do usuário
	public void setNome(String nome) {
		this.nome = nome;
	}

	// Método que retorna o nome de usuário (geralmente o e-mail ou o nome de usuário único)
	public String getUsuario() {
		return this.usuario;
	}

	// Método que define o nome de usuário
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	// Método que retorna a senha do usuário
	public String getSenha() {
		return this.senha;
	}

	// Método que define a senha do usuário
	public void setSenha(String senha) {
		this.senha = senha;
	}

	// Método que retorna a foto do usuário (pode ser a URL da foto ou um caminho)
	public String getFoto() {
		return this.foto;
	}

	// Método que define a foto do usuário
	public void setFoto(String foto) {
		this.foto = foto;
	}

	// Método que retorna o token gerado para autenticação do usuário
	public String getToken() {
		return this.token;
	}

	// Método que define o token de autenticação do usuário
	public void setToken(String token) {
		this.token = token;
	}
}
