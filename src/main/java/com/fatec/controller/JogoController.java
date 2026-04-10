package com.fatec.controller;

import com.fatec.model.Jogos;
import com.fatec.service.JogosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/jogos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class JogoController {

    private final JogosService jogosService;

    @Autowired
    public JogoController(JogosService jogosService) {
        this.jogosService = jogosService;
    }

    @GetMapping
    public ResponseEntity<List<Jogos>> getAll() {
        return ResponseEntity.ok(jogosService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jogos> getById(@PathVariable Long id) {
        return ResponseEntity.ok(jogosService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Jogos> create(@Valid @RequestBody Jogos jogo) {
        Jogos criado = jogosService.create(jogo);
        return ResponseEntity.created(URI.create("/jogos/" + criado.getId()))
                .body(criado);
    }

    @PutMapping
    public ResponseEntity<Jogos> update(@Valid @RequestBody Jogos jogo) {
        return ResponseEntity.ok(jogosService.update(jogo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        jogosService.delete(id);
        return ResponseEntity.noContent().build();
    }
}