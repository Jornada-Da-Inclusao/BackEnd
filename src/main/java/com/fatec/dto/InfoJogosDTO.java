package com.fatec.dto;

import java.time.LocalDateTime;

public class InfoJogosDTO {

    private Long id;
    private Long tempoTotal; // em ms
    private Long totalTentativas;
    private Long totalAcertos;
    private Long totalErros;

    private Long jogoId;
    private Long dependenteId;

    private String nomeJogo;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public InfoJogosDTO() {
    }

    public InfoJogosDTO(Long id, Long tempoTotal, Long totalTentativas, Long totalAcertos, Long totalErros,
                        Long jogoId, Long dependenteId, String nomeJogo,
                        LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.tempoTotal = tempoTotal;
        this.totalTentativas = totalTentativas;
        this.totalAcertos = totalAcertos;
        this.totalErros = totalErros;
        this.jogoId = jogoId;
        this.dependenteId = dependenteId;
        this.nomeJogo = nomeJogo;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTempoTotal() {
        return tempoTotal;
    }

    public void setTempoTotal(Long tempoTotal) {
        this.tempoTotal = tempoTotal;
    }

    public Long getTotalTentativas() {
        return totalTentativas;
    }

    public void setTotalTentativas(Long totalTentativas) {
        this.totalTentativas = totalTentativas;
    }

    public Long getTotalAcertos() {
        return totalAcertos;
    }

    public void setTotalAcertos(Long totalAcertos) {
        this.totalAcertos = totalAcertos;
    }

    public Long getTotalErros() {
        return totalErros;
    }

    public void setTotalErros(Long totalErros) {
        this.totalErros = totalErros;
    }

    public Long getJogoId() {
        return jogoId;
    }

    public void setJogoId(Long jogoId) {
        this.jogoId = jogoId;
    }

    public Long getDependenteId() {
        return dependenteId;
    }

    public void setDependenteId(Long dependenteId) {
        this.dependenteId = dependenteId;
    }

    public String getNomeJogo() {
        return nomeJogo;
    }

    public void setNomeJogo(String nomeJogo) {
        this.nomeJogo = nomeJogo;
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
}