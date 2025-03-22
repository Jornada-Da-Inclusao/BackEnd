package com.fatec.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// A anotação @Entity indica que a classe é uma entidade JPA, ou seja, ela será mapeada para uma tabela no banco de dados.
@Entity
// A anotação @Table define o nome da tabela que será associada a esta entidade no banco de dados.
@Table(name = "tb_usuarios")
public class Usuario {

	// A anotação @Id marca o campo como a chave primária da tabela
	@Id
	// A anotação @GeneratedValue define como o ID será gerado. O valor será gerado automaticamente pelo banco de dados.
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	// A anotação @NotBlank garante que o atributo não pode ser vazio.
	@NotBlank(message = "O atributo Nome é obrigatório.")
	private String nome;

	// A anotação @Schema é usada para gerar documentação da API com o Swagger. O atributo "example" especifica um exemplo do valor para esse campo.
	@Schema(example = "email@email.com.br")
	// @NotBlank valida que o atributo não pode ser vazio.
	// @Email garante que o valor fornecido seja um e-mail válido.
	@NotBlank(message = "O atributo Usuário é obrigatório")
	@Email(message = "O atributo Usuário deve ser um email válido")
	private String usuario;

	// Foto do usuário (não obrigatório)
	private String foto;

	// A anotação @NotBlank valida que o atributo senha não pode ser vazio.
	// A anotação @Size(min = 8) valida que a senha deve ter pelo menos 8 caracteres.
	@NotBlank(message = "O atributo senha é obrigatório.")
	@Size(min = 8)
	private String senha;

	// Getters e setters para acessar e modificar os valores dos atributos

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
