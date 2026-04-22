package com.fatec.repository;

import com.fatec.dto.DependenteDTO;
import com.fatec.model.Dependente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DependenteDTORepository extends JpaRepository<Dependente, Long> {

    @Query("""
                SELECT new com.fatec.dto.DependenteDTO(
                    d.id,
                    d.nome,
                    d.dataNascimento,
                    d.sexo,
                    d.foto
                )
                FROM Dependente d
                WHERE d.usuario.id = :usuarioId
            """)
    List<DependenteDTO> findDTOByUsuarioId(@Param("usuarioId") Long usuarioId);
}