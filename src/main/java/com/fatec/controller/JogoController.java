package com.fatec.controller;

import com.fatec.model.Jogos;
import com.fatec.repository.JogosRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;


@RestController  // Anotação que define esta classe como um controlador REST
@RequestMapping("/jogos")  // Define o caminho base para as requisições dessa classe
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class JogoController {


    @Autowired
    private JogosRepository jogosRepository;

    @GetMapping
    public ResponseEntity<List<Jogos>> getAll() {
        return ResponseEntity.ok(jogosRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jogos> getById(@PathVariable Long id) {
        return jogosRepository.findById(id)
                .map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Jogos> post(@Valid @RequestBody Jogos jogo) {
        Jogos jogoCriado = jogosRepository.save(jogo);
        return ResponseEntity.created(URI.create("/jogos/" + jogoCriado.getId()))
                .body(jogoCriado);
    }

    @PutMapping()
    public ResponseEntity<Jogos> put(@Valid @RequestBody Jogos jogo) {
        if (!jogosRepository.existsById(jogo.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        jogo.setId(jogo.getId()); // Certifica-se de que o ID está correto para a atualização
        return ResponseEntity.status(HttpStatus.OK)
                .body(jogosRepository.save(jogo));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        jogosRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogo não encontrado"));

        jogosRepository.deleteById(id);
    }
}
