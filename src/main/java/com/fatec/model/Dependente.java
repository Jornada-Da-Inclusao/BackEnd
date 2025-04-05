package com.fatec.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
@Table(name = "tb_dependentes")
public class Dependente {
    @Id
    // A anotação @GeneratedValue define como o ID será gerado. O valor será gerado automaticamente pelo banco de dados.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "O atributo nome é obrigatório.")
    private String nome;

    @NotNull(message = "O atributo idade é obrigatório.")
    private Integer idade;

    @NotBlank(message = "O atributo sexo é obrigatório.")
    private String sexo;

    private Date data_atualizacao;

    private Date data_criacao;

    @ManyToOne
    @JoinColumn(name = "usuario_id_fk")
    @JsonIgnoreProperties("Usuario")
    private Usuario usuario_id_fk;


    @PrePersist
    public void prePersist() {
        if (this.data_criacao == null) {
            this.data_criacao = new Date();
        }
        this.data_atualizacao = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        this.data_atualizacao = new Date();
    }

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

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Date getData_atualizacao() {
        return data_atualizacao;
    }

    public void setData_atualizacao(Date data_atualizacao) {
        this.data_atualizacao = data_atualizacao;
    }

    public Date getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(Date data_criacao) {
        this.data_criacao = data_criacao;
    }

    public Usuario getUsuario_id_fk() {
        return usuario_id_fk;
    }

    public void setUsuario_id_fk(Usuario usuario_id_fk) {
        this.usuario_id_fk = usuario_id_fk;
    }
}
