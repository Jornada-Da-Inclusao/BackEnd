package com.fatec.repository;

import com.fatec.model.Dependente;
import com.fatec.model.InfoJogos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InfoJogosRepository extends JpaRepository<InfoJogos, Long> {
    List<InfoJogos> findByDependenteId(Long dependenteId);
}
