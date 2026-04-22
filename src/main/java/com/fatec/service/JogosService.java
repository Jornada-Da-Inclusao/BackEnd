package com.fatec.service;

import com.fatec.exception.AppException;
import com.fatec.exception.ErrorCode;
import com.fatec.model.Jogos;
import com.fatec.repository.JogosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JogosService {

    private final JogosRepository jogosRepository;

    @Autowired
    public JogosService(JogosRepository jogosRepository) {
        this.jogosRepository = jogosRepository;
    }

    public List<Jogos> getAll() {
        return jogosRepository.findAll();
    }

    public Jogos getById(Long id) {
        return jogosRepository.findById(id)
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND.getCode(),
                        "Jogo não encontrado",
                        HttpStatus.NOT_FOUND
                ));
    }

    public Jogos create(Jogos jogo) {
        return jogosRepository.save(jogo);
    }

    public Jogos update(Jogos jogo) {
        Jogos existing = jogosRepository.findById(jogo.getId())
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND.getCode(),
                        "Jogo não encontrado",
                        HttpStatus.NOT_FOUND
                ));
        existing.setNomeJogo(jogo.getNomeJogo());
        existing.setDificuldade(jogo.getDificuldade());
        return jogosRepository.save(existing);
    }

    public void delete(Long id) {
        Jogos existing = jogosRepository.findById(id)
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND.getCode(),
                        "Jogo não encontrado",
                        HttpStatus.NOT_FOUND
                ));
        jogosRepository.delete(existing);
    }
}