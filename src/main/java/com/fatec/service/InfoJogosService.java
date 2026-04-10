package com.fatec.service;

import com.fatec.exception.AppException;
import com.fatec.exception.ErrorCode;
import com.fatec.model.Dependente;
import com.fatec.model.InfoJogos;
import com.fatec.model.Jogos;
import com.fatec.repository.DependenteRepository;
import com.fatec.repository.InfoJogosRepository;
import com.fatec.repository.JogosRepository;
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

    public InfoJogos getById(Long id) {
        return infoJogosRepository.findById(id)
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND.getCode(),
                        "InfoJogos não encontrado",
                        HttpStatus.NOT_FOUND
                ));
    }

    public InfoJogos create(InfoJogos infoJogos) {
        Jogos jogo = jogosRepository.findById(infoJogos.getInfoJogos_id_fk().getId())
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

        infoJogos.setInfoJogos_id_fk(jogo);
        infoJogos.setDependente(dependente);

        // createDate e updateDate são preenchidos automaticamente pelo JPA
        return infoJogosRepository.save(infoJogos);
    }

    public InfoJogos update(InfoJogos infoJogos) {
        InfoJogos existing = infoJogosRepository.findById(infoJogos.getId())
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND.getCode(),
                        "InfoJogos não encontrado",
                        HttpStatus.NOT_FOUND
                ));

        Jogos jogo = jogosRepository.findById(infoJogos.getInfoJogos_id_fk().getId())
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND.getCode(),
                        "Jogo não encontrado",
                        HttpStatus.BAD_REQUEST
                ));

        existing.setInfoJogos_id_fk(jogo);
        existing.setDependente(infoJogos.getDependente()); // Caso queira atualizar dependente
        existing.setTempoTotal(infoJogos.getTempoTotal());
        existing.setTentativas(infoJogos.getTentativas());
        existing.setAcertos(infoJogos.getAcertos());
        existing.setErros(infoJogos.getErros());

        // updateDate será atualizado automaticamente pelo JPA
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