package com.fatec.controller;

import com.fatec.model.Dependente;
import com.fatec.model.InfoJogos;
import com.fatec.model.Usuario;
import com.fatec.repository.DependenteRepository;
import com.fatec.repository.InfoJogosRepository;
import com.fatec.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController  // Anotação que define esta classe como um controlador REST
@RequestMapping("/dependente")  // Define o caminho base para as requisições dessa classe
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DependenteController {
    @Autowired
    private DependenteRepository dependenteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private InfoJogosRepository infoJogosRepository;

    @GetMapping
    public ResponseEntity<List<Dependente>> getAll() {
        // Agora, usamos a instância do repositório para chamar o método findAll()
        return ResponseEntity.ok(dependenteRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dependente> getById(@PathVariable Long id){
        return dependenteRepository.findById(id)
                .map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/infoJogosByDependente/{id}")
    public ResponseEntity<List<InfoJogos>> getInfoJogosByDependente(@PathVariable Long id) {
        // Busca todos os InfoJogos associados ao dependente pelo ID
        List<InfoJogos> infoJogosList = infoJogosRepository.findByDependenteId(id);

        if (infoJogosList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Caso não haja jogos associados
        }

        return ResponseEntity.ok(infoJogosList); // Retorna a lista de InfoJogos
    }




    @PostMapping
    public ResponseEntity<Dependente> post(@Valid @RequestBody Dependente dependente) {
        // Verifica se o usuário associado ao dependente existe
        Usuario usuario = usuarioRepository.findById(dependente.getUsuario_id_fk().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não existe!"));

        // Se o usuário existir, associa ao dependente e salva
        dependente.setUsuario_id_fk(usuario);

        // Salva o dependente no banco
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dependenteRepository.save(dependente));
    }


    @PutMapping
    public ResponseEntity<Dependente> put(@Valid @RequestBody Dependente dependente){
        // Verifica se o usuário associado ao dependente existe
        Usuario usuario = usuarioRepository.findById(dependente.getUsuario_id_fk().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não existe!"));

        // Se o usuário existir, associa ao dependente e salva
        dependente.setUsuario_id_fk(usuario);

        // Salva o dependente no banco
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dependenteRepository.save(dependente));

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Optional<Dependente> dependente = dependenteRepository.findById(id);

        if(dependente.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        dependenteRepository.deleteById(id);
    }
}
