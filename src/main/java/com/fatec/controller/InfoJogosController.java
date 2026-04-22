package com.fatec.controller;

import com.fatec.dto.InfoJogosDTO;
import com.fatec.model.InfoJogos;
import com.fatec.repository.projection.InfoJogosProjection;
import com.fatec.service.InfoJogosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

//    @GetMapping("/{id}")
//    public ResponseEntity<InfoJogosDTO> getById(@PathVariable Long id) {
//        return ResponseEntity.ok(infoJogosService.getById(id));
//    }

    @GetMapping("/dependente/{id}")
    public ResponseEntity<List<InfoJogosProjection>> getByDependenteId(@PathVariable Long id) {
        return ResponseEntity.ok(infoJogosService.getByDependenteId(id));
    }

    @PostMapping
    public ResponseEntity<InfoJogos> create(@Valid @RequestBody InfoJogos infoJogos) {
        infoJogosService.create(infoJogos);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping
    public ResponseEntity<InfoJogos> update(@Valid @RequestBody InfoJogosDTO dto) {
        return ResponseEntity.ok(infoJogosService.update(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        infoJogosService.delete(id);
        return ResponseEntity.noContent().build();
    }
}