package com.fatec.controller;


import com.fatec.model.Dependente;
import com.fatec.model.InfoJogos;
import com.fatec.model.Jogos;
import com.fatec.model.Usuario;
import com.fatec.repository.DependenteRepository;
import com.fatec.repository.InfoJogosRepository;
import com.fatec.repository.JogosRepository;
import com.fatec.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController  // Anotação que define esta classe como um controlador REST
@RequestMapping("/infoJogos")  // Define o caminho base para as requisições dessa classe
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InfoJogosController {


    @Autowired
    private InfoJogosRepository infoJogosRepository;

    @Autowired
    private JogosRepository jogosRepository;

    @Autowired
    private DependenteRepository dependenteRepository;

    @GetMapping
    public ResponseEntity<List<InfoJogos>> getAll() {
        return ResponseEntity.ok(infoJogosRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InfoJogos> getById(@PathVariable Long id) {
        return infoJogosRepository.findById(id)
                .map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @PostMapping
    public ResponseEntity<InfoJogos> post(@Valid @RequestBody InfoJogos infoJogos) {
        // Verifica se o jogo associado existe
        Jogos jogo = jogosRepository.findById(infoJogos.getInfoJogos_id_fk().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Jogo não existe!"));

        // Verifica se o dependente associado existe
        Dependente dependente = dependenteRepository.findById(infoJogos.getDependente().getId()) // Assuming you're passing the dependente in infoJogos
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dependente não encontrado!"));

        // Se o jogo e o dependente existirem, associa-los ao infoJogos
        infoJogos.setInfoJogos_id_fk(jogo); // Associando o Jogo
        infoJogos.setDependente(dependente); // Associando o Dependente

        // Salva o InfoJogos no banco de dados
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(infoJogosRepository.save(infoJogos));
    }


    @PutMapping()
    public ResponseEntity<InfoJogos> put(@Valid @RequestBody InfoJogos infoJogos) {
        if (!infoJogosRepository.existsById(infoJogos.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Verifica se o jogo existe
        Jogos jogo = jogosRepository.findById(infoJogos.getInfoJogos_id_fk().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Jogo não encontrado!"));

        // Atualiza o jogo associado
        infoJogos.setInfoJogos_id_fk(jogo);

        // Garante que a data_atualizacao seja sempre atualizada com a data atual no PUT
        infoJogos.setUpdateDate(LocalDateTime.now());  // Atualiza a data de atualização com a data/hora atuais
          // Atualiza a data de atualização

        // Não precisa fazer nada com a data_criacao, pois ela já foi preenchida na criação (POST)
        // O campo data_criacao será mantido com o valor original

        // Retorna o objeto atualizado com os campos novos e o campo de criação antigo
        return ResponseEntity.status(HttpStatus.OK)
                .body(infoJogosRepository.save(infoJogos));  // A data_criacao permanece intacta
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        infoJogosRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "InfoJogos não encontrado"));

        infoJogosRepository.deleteById(id);
    }
}
