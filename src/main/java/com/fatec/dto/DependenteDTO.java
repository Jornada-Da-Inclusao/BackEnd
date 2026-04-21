package com.fatec.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public class DependenteDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome não pode ter mais de 100 caracteres")
    private String nome;

    @Min(value = 0, message = "A dataNascimento deve ser maior ou igual a zero")
    private Instant dataNascimento;

    @NotBlank(message = "O sexo é obrigatório")
    @Size(max = 20, message = "O valor do sexo não pode ter mais de 20 caracteres")
    private String sexo;

    private Long id;

    private String foto;

    public DependenteDTO(Long id, String nome, Instant  dataNascimento, String sexo, String foto) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.foto = foto;
    }
    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Instant  getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Instant  dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}