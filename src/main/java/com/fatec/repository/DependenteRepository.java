package com.fatec.repository;

import com.fatec.model.Dependente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DependenteRepository extends JpaRepository<Dependente, Long> {

    // Renomeado para refletir mais claramente o relacionamento
    Optional<Dependente> findByNome(String nome);

    // Renomeado para refletir o relacionamento correto e mais claro
    List<Dependente> findByEmailId(Long usuarioId);

    List<Dependente> findByEmail_Id(Long usuarioId);
}
