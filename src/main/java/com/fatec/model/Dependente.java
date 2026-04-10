package com.fatec.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_dependentes")
public class Dependente {

    @Id
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

    // Foto do dependente (não obrigatório)
    private String foto;

    @ManyToOne
    @JoinColumn(name = "email_id_fk")
    @JsonIgnoreProperties("Email")
    @JsonIgnore
    private Usuario email;

    @OneToMany(mappedBy = "dependente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("dependente")
    @JsonIgnore
    private List<InfoJogos> infoJogos;

    // Método de persistência para garantir que as datas sejam definidas corretamente
    @PrePersist
    public void prePersist() {
        if (this.data_criacao == null) {
            this.data_criacao = new Date();
        }
        this.data_atualizacao = new Date();
    }

    // Método de atualização para garantir que a data de atualização seja definida corretamente
    @PreUpdate
    public void preUpdate() {
        this.data_atualizacao = new Date();
    }
    public List<InfoJogos> getInfoJogos() {
        return infoJogos;
    }

    public void setInfoJogos(List<InfoJogos> infoJogos) {
        this.infoJogos = infoJogos;
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

    public Usuario getEmail_id_fk() {
        return email;
    }

    public void setEmail_id_fk(Usuario email_id_fk) {
        this.email = email_id_fk;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
