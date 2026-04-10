package com.fatec.controller;

import com.fatec.model.InfoJogos;
import com.fatec.service.InfoJogosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/infoJogos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InfoJogosController {

    private final InfoJogosService infoJogosService;

    @Autowired
    public InfoJogosController(InfoJogosService infoJogosService) {
        this.infoJogosService = infoJogosService;
    }

    @GetMapping
    public ResponseEntity<List<InfoJogos>> getAll() {
        return ResponseEntity.ok(infoJogosService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InfoJogos> getById(@PathVariable Long id) {
        return ResponseEntity.ok(infoJogosService.getById(id));
    }

    @PostMapping
    public ResponseEntity<InfoJogos> create(@Valid @RequestBody InfoJogos infoJogos) {
        return ResponseEntity.status(201).body(infoJogosService.create(infoJogos));
    }

    @PutMapping
    public ResponseEntity<InfoJogos> update(@Valid @RequestBody InfoJogos infoJogos) {
        return ResponseEntity.ok(infoJogosService.update(infoJogos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        infoJogosService.delete(id);
        return ResponseEntity.noContent().build();
    }
}