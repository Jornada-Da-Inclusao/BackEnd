package com.fatec.repository;

import com.fatec.dto.InfoJogosDTO;
import com.fatec.model.Dependente;
import com.fatec.model.InfoJogos;
import com.fatec.repository.projection.InfoJogosProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InfoJogosRepository extends JpaRepository<InfoJogos, Long> {
    @Query("""
            SELECT 
                i.id as id,
                i.tempoTotal as tempoTotal,
                i.totalTentativas as totalTentativas,
                i.totalAcertos as totalAcertos,
                i.totalErros as totalErros,
                j.id as jogoId,
                d.id as dependenteId,
                i.createDate as createDate,
                i.updateDate as updateDate,
                j.nomeJogo as nomeJogo
            FROM InfoJogos i
            JOIN i.jogo j
            JOIN i.dependente d
            WHERE d.id = :dependenteId
            """)
    List<InfoJogosProjection> findDTOByDependenteId(Long dependenteId);
    List<InfoJogos> findByDependenteId(Long dependenteId);
}
