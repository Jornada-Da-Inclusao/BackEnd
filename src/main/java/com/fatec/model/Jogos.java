package com.fatec.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "tb_jogos")
public class Jogos {

    @Id
    // A anotação @GeneratedValue define como o ID será gerado. O valor será gerado automaticamente pelo banco de dados.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "O atributo Nome é obrigatório.")
    @Size(max = 100, message = "Nome do jogo não pode ter mais de 100 caracteres.")
    private String nome;

    @NotBlank(message = "O atributo dificuldade é obrigatório.")
    @Size(max = 20, message = "Nome do jogo não pode ter mais de 20 caracteres.")
    private String dificuldade;


    @OneToMany(mappedBy = "infoJogos_id_fk", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("info_jogos_id_fk") // Ignora a referência a InfoJogos em InfoJogos
    @JsonIgnore
    private List<InfoJogos> infoJogos;


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

    public String getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(String dificuldade) {
        this.dificuldade = dificuldade;
    }

    public List<InfoJogos> getInfoJogos() {
        return infoJogos;
    }

    public void setInfoJogos(List<InfoJogos> infoJogos) {
        this.infoJogos = infoJogos;
    }
}
