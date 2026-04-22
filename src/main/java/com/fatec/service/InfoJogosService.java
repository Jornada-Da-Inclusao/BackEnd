package com.fatec.service;

import com.fatec.dto.InfoJogosDTO;
import com.fatec.exception.AppException;
import com.fatec.exception.ErrorCode;
import com.fatec.model.Dependente;
import com.fatec.model.InfoJogos;
import com.fatec.model.Jogos;
import com.fatec.repository.DependenteRepository;
import com.fatec.repository.InfoJogosRepository;
import com.fatec.repository.JogosRepository;
import com.fatec.repository.projection.InfoJogosProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfoJogosService {

    private final InfoJogosRepository infoJogosRepository;
    private final JogosRepository jogosRepository;
    private final DependenteRepository dependenteRepository;

    @Autowired
    public InfoJogosService(InfoJogosRepository infoJogosRepository,
                            JogosRepository jogosRepository,
                            DependenteRepository dependenteRepository) {
        this.infoJogosRepository = infoJogosRepository;
        this.jogosRepository = jogosRepository;
        this.dependenteRepository = dependenteRepository;
    }

    public List<InfoJogos> getAll() {
        return infoJogosRepository.findAll();
    }

    public List<InfoJogosProjection> getByDependenteId(Long dependenteId) {
        List<InfoJogosProjection> result = infoJogosRepository.findDTOByDependenteId(dependenteId);

        if (result.isEmpty()) {
            throw new AppException(
                    ErrorCode.RESOURCE_NOT_FOUND.getCode(),
                    "Nenhum InfoJogos encontrado para dependente id: " + dependenteId,
                    HttpStatus.NOT_FOUND
            );
        }

        return result;
    }

    public InfoJogos create(InfoJogos infoJogos) {

        Jogos jogo = jogosRepository.findById(infoJogos.getJogo().getId())
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND.getCode(),
                        "Jogo não encontrado",
                        HttpStatus.BAD_REQUEST
                ));

        Dependente dependente = dependenteRepository.findById(infoJogos.getDependente().getId())
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND.getCode(),
                        "Dependente não encontrado",
                        HttpStatus.BAD_REQUEST
                ));

        infoJogos.setJogo(jogo);
        infoJogos.setDependente(dependente);

        return infoJogosRepository.save(infoJogos);
    }

    public InfoJogos update(InfoJogosDTO dto) {

        InfoJogos existing = infoJogosRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND.getCode(),
                        "InfoJogos não encontrado",
                        HttpStatus.NOT_FOUND
                ));

        // Jogo
        if (dto.getJogoId() != null) {
            Jogos jogo = jogosRepository.findById(dto.getJogoId())
                    .orElseThrow(() -> new AppException(
                            ErrorCode.RESOURCE_NOT_FOUND.getCode(),
                            "Jogo não encontrado",
                            HttpStatus.BAD_REQUEST
                    ));
            existing.setJogo(jogo);
        }

        // Dependente
        if (dto.getDependenteId() != null) {
            Dependente dependente = dependenteRepository.findById(dto.getDependenteId())
                    .orElseThrow(() -> new AppException(
                            ErrorCode.RESOURCE_NOT_FOUND.getCode(),
                            "Dependente não encontrado",
                            HttpStatus.BAD_REQUEST
                    ));
            existing.setDependente(dependente);
        }

        // Métricas (patch-safe)
        if (dto.getTempoTotal() != null)
            existing.setTempoTotal(dto.getTempoTotal());

        if (dto.getTotalTentativas() != null)
            existing.setTotalTentativas(dto.getTotalTentativas());

        if (dto.getTotalAcertos() != null)
            existing.setTotalAcertos(dto.getTotalAcertos());

        if (dto.getTotalErros() != null)
            existing.setTotalErros(dto.getTotalErros());

        return infoJogosRepository.save(existing);
    }

    public void delete(Long id) {
        InfoJogos existing = infoJogosRepository.findById(id)
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND.getCode(),
                        "InfoJogos não encontrado",
                        HttpStatus.NOT_FOUND
                ));
        infoJogosRepository.delete(existing);
    }
}