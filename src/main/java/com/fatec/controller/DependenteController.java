package com.fatec.controller;

import com.fatec.dto.DependenteDTO;
import com.fatec.model.Dependente;
import com.fatec.model.InfoJogos;
import com.fatec.service.DependenteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dependentes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DependenteController {

    private final DependenteService dependenteService;

    @Autowired
    public DependenteController(DependenteService dependenteService) {
        this.dependenteService = dependenteService;
    }

    @GetMapping
    public ResponseEntity<List<Dependente>> getAll() {
        return ResponseEntity.ok(dependenteService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dependente> getById(@PathVariable Long id) {
        return ResponseEntity.ok(dependenteService.getById(id));
    }

    @GetMapping("/infoJogos/{id}")
    public ResponseEntity<List<InfoJogos>> getInfoJogosByDependente(@PathVariable Long id) {
        return ResponseEntity.ok(dependenteService.getInfoJogosByDependente(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<DependenteDTO>> getDependentesByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(dependenteService.getDependentesByUsuarioId(usuarioId));
    }

    @PostMapping
    public ResponseEntity<Dependente> create(@Valid @RequestBody Dependente dependente) {
        return ResponseEntity.status(201).body(dependenteService.create(dependente));
    }

    @PutMapping
    public ResponseEntity<Dependente> update(@Valid @RequestBody Dependente dependente) {
        return ResponseEntity.ok(dependenteService.update(dependente));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Dependente> updatePartial(@PathVariable Long id,
                                                    @RequestBody DependenteDTO dto) {
        dependenteService.updatePartial(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dependenteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}