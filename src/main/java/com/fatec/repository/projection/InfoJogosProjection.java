package com.fatec.repository.projection;

import java.time.LocalDateTime;

public interface InfoJogosProjection {

    Long getId();

    Long getTempoTotal();

    Long getTotalTentativas();

    Long getTotalAcertos();

    Long getTotalErros();

    Long getJogoId();

    Long getDependenteId();

    LocalDateTime getCreateDate();

    LocalDateTime getUpdateDate();

    String getNomeJogo();
}
