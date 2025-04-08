package com.fatec.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Table(name = "tb_infoJogos")
public class InfoJogos {
    @Id
    // A anotação @GeneratedValue define como o ID será gerado. O valor será gerado automaticamente pelo banco de dados.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Min(0)
    private long tempoTotal;

    @Min(0)
    private long tentativas;

    @Min(0)
    private long acertos;

    @Min(0)
    private long erros;


    @ManyToOne
    @JoinColumn(name = "infoJogos_id_fk")
    @JsonIgnoreProperties("infoJogos") // Ignora a propriedade infoJogos em Jogos
    private Jogos infoJogos_id_fk;

    @ManyToOne
    @JoinColumn(name = "dependente_id_fk")
    @JsonIgnoreProperties("infoJogos")
    private Dependente dependente;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @PrePersist
    public void onPrePersist() {
        this.setCreateDate(LocalDateTime.now());
        this.setUpdateDate(LocalDateTime.now());
    }

    @PreUpdate
    public void onPreUpdate() {
        this.setUpdateDate(LocalDateTime.now());
    }

    public Dependente getDependente() {
        return dependente;
    }

    public void setDependente(Dependente dependente) {
        this.dependente = dependente;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTempoTotal() {
        return tempoTotal;
    }

    public void setTempoTotal(long tempoTotal) {
        this.tempoTotal = tempoTotal;
    }

    public long getTentativas() {
        return tentativas;
    }

    public void setTentativas(long tentativas) {
        this.tentativas = tentativas;
    }

    public long getAcertos() {
        return acertos;
    }

    public void setAcertos(long acertos) {
        this.acertos = acertos;
    }

    public long getErros() {
        return erros;
    }

    public void setErros(long erros) {
        this.erros = erros;
    }

    public Jogos getInfoJogos_id_fk() {
        return infoJogos_id_fk;
    }

    public void setInfoJogos_id_fk(Jogos infoJogos_id_fk) {
        this.infoJogos_id_fk = infoJogos_id_fk;
    }

}
