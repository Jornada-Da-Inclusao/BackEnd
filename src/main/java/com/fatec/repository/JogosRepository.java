package com.fatec.repository;

import com.fatec.model.Jogos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JogosRepository extends JpaRepository<Jogos, Long> {
    public Optional<Jogos> findByNome(String nome);
}
