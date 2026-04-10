package com.fatec.service;

import com.fatec.dto.DependenteDto;
import com.fatec.exception.AppException;
import com.fatec.exception.ErrorCode;
import com.fatec.model.Dependente;
import com.fatec.model.InfoJogos;
import com.fatec.model.Usuario;
import com.fatec.repository.DependenteRepository;
import com.fatec.repository.InfoJogosRepository;
import com.fatec.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DependenteService {

    private final DependenteRepository dependenteRepository;
    private final UsuarioRepository usuarioRepository;
    private final InfoJogosRepository infoJogosRepository;

    @Autowired
    public DependenteService(DependenteRepository dependenteRepository,
                             UsuarioRepository usuarioRepository,
                             InfoJogosRepository infoJogosRepository) {
        this.dependenteRepository = dependenteRepository;
        this.usuarioRepository = usuarioRepository;
        this.infoJogosRepository = infoJogosRepository;
    }

    // ===================== CRUD =====================

    public List<Dependente> getAll() {
        return dependenteRepository.findAll();
    }

    public Dependente getById(Long id) {
        return dependenteRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND.getCode(),
                        "Dependente não encontrado", HttpStatus.NOT_FOUND));
    }

    public List<InfoJogos> getInfoJogosByDependente(Long id) {
        List<InfoJogos> jogos = infoJogosRepository.findByDependenteId(id);
        if (jogos.isEmpty()) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND.getCode(),
                    "Nenhum jogo encontrado para este dependente", HttpStatus.NOT_FOUND);
        }
        return jogos;
    }

    public List<Dependente> getDependentesByUsuarioId(Long usuarioId) {
        List<Dependente> dependentes = dependenteRepository.findByEmail_Id(usuarioId);
        if (dependentes.isEmpty()) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND.getCode(),
                    "Nenhum dependente encontrado para este usuário", HttpStatus.NOT_FOUND);
        }
        return dependentes;
    }

    public Dependente create(Dependente dependente) {
        Usuario usuario = usuarioRepository.findById(dependente.getEmail_id_fk().getId())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REQUEST.getCode(),
                        "Usuário não existe!", HttpStatus.BAD_REQUEST));
        dependente.setEmail_id_fk(usuario);
        return dependenteRepository.save(dependente);
    }

    public Dependente update(Dependente dependente) {
        Dependente existing = dependenteRepository.findById(dependente.getId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND.getCode(),
                        "Dependente não encontrado", HttpStatus.NOT_FOUND));

        Usuario usuario = usuarioRepository.findById(dependente.getEmail_id_fk().getId())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REQUEST.getCode(),
                        "Usuário não existe!", HttpStatus.BAD_REQUEST));

        existing.setNome(dependente.getNome());
        existing.setIdade(dependente.getIdade());
        existing.setSexo(dependente.getSexo());
        existing.setEmail_id_fk(usuario);

        return dependenteRepository.save(existing);
    }

    public Dependente updatePartial(Long id, DependenteDto dto) {
        Dependente existing = dependenteRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND.getCode(),
                        "Dependente não encontrado", HttpStatus.NOT_FOUND));

        if (dto.getNome() != null) existing.setNome(dto.getNome());
        if (dto.getIdade() != null) existing.setIdade(dto.getIdade());
        if (dto.getSexo() != null) existing.setSexo(dto.getSexo());

        return dependenteRepository.save(existing);
    }

    public void delete(Long id) {
        Dependente existing = dependenteRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND.getCode(),
                        "Dependente não encontrado", HttpStatus.NOT_FOUND));

        dependenteRepository.deleteById(id);
    }
}