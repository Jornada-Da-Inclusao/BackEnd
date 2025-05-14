package com.fatec.controller;

import com.fatec.dto.DependenteDTO;
import com.fatec.model.Dependente;
import com.fatec.model.InfoJogos;
import com.fatec.model.Usuario;
import com.fatec.repository.DependenteRepository;
import com.fatec.repository.InfoJogosRepository;
import com.fatec.repository.UsuarioRepository;
import com.fatec.service.ExcelExportService;
import com.fatec.service.PdfExportService;
import com.itextpdf.text.DocumentException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;
import java.io.IOException;

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

    @Autowired
    private ExcelExportService excelExportService;

    @Autowired
    private PdfExportService pdfExportService;

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

    @GetMapping("/getDependenteByIdUsuario/{id}")
    public ResponseEntity<List<Dependente>> getDependenteByIdUsuario(@PathVariable Long id) {
        // Busca a lista de dependentes pelo ID do usuário
        List<Dependente> usuarioList = dependenteRepository.findByUsuario_Id(id);

        // Verifica se a lista está vazia
        if (usuarioList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Retorna 404 se não houver dependentes
        }

        // Retorna a lista de dependentes com status 200
        return ResponseEntity.ok(usuarioList);
    }


    // Endpoint para exportar para Excel, com base no dependente
    @GetMapping("/exportExcel/{id}")
    public ResponseEntity<byte[]> exportToExcel(@PathVariable Long id) throws IOException {
        // Recupera os dados de InfoJogos para o dependente com o ID fornecido
        List<InfoJogos> infoJogosList = getDataFromDatabase(id);

        // Recupera os dados do Dependente
        Dependente dependente = dependenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dependente não encontrado"));

        if (infoJogosList.isEmpty()) {
            return ResponseEntity.status(404).body(null);  // Não encontrou jogos
        }

        // Gerando o Excel a partir dos dados
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        excelExportService.exportDataToExcel(infoJogosList, dependente, byteArrayOutputStream);  // Passando o dependente

        byte[] excelData = byteArrayOutputStream.toByteArray();

        // Definindo o cabeçalho para o download
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio.xlsx");

        return ResponseEntity.ok().headers(headers).body(excelData);
    }


    // Endpoint para exportar para PDF, com base no dependente
    @GetMapping("/exportPdf/{id}")
    public ResponseEntity<byte[]> exportToPdf(@PathVariable Long id) {
        try {
            // Recupera os dados de InfoJogos para o dependente com o ID fornecido
            List<InfoJogos> infoJogosList = getDataFromDatabase(id);

            // Recupera os dados do Dependente
            Dependente dependente = dependenteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Dependente não encontrado"));

            if (infoJogosList.isEmpty()) {
                return ResponseEntity.status(404).body(null);  // Não encontrou jogos
            }

            // Gerando o PDF a partir dos dados
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            pdfExportService.exportDataToPdf(infoJogosList, dependente, byteArrayOutputStream);

            byte[] pdfData = byteArrayOutputStream.toByteArray();

            // Definindo o cabeçalho para o download
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio.pdf");

            return ResponseEntity.ok().headers(headers).body(pdfData);

        } catch (Exception e) {
            // Loga o erro e retorna um erro genérico
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao gerar o PDF".getBytes());
        }
    }



    // Método para obter os dados de InfoJogos de um dependente
    private List<InfoJogos> getDataFromDatabase(Long dependenteId) {
        // Buscando os InfoJogos associados ao dependente pelo ID
        return infoJogosRepository.findByDependenteId(dependenteId);
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

    @PatchMapping("/{id}")
    public ResponseEntity<Dependente> patch(@PathVariable Long id, @RequestBody DependenteDTO dependenteDTO) {
        // Verifica se o dependente existe
        Dependente dependenteExistente = dependenteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dependente não encontrado"));

        // Verifica se o usuário associado ao dependente existe
        // Aqui você não precisa modificar a FK do usuário. Ele já está associado
        Usuario usuario = dependenteExistente.getUsuario_id_fk();  // Mantém o usuário existente

        // Atualiza os campos do dependente com os dados do DTO, se fornecido
        if (dependenteDTO.getNome() != null) {
            dependenteExistente.setNome(dependenteDTO.getNome());
        } else {
            // Define um valor padrão caso o nome não seja fornecido
            dependenteExistente.setNome("Nome Padrão");
        }

        if (dependenteDTO.getIdade() != null) {
            dependenteExistente.setIdade(dependenteDTO.getIdade());
        } else {
            // Define uma idade padrão, caso a idade não seja fornecida
            dependenteExistente.setIdade(18);  // Exemplo de idade padrão
        }

        if (dependenteDTO.getSexo() != null) {
            dependenteExistente.setSexo(dependenteDTO.getSexo());
        } else {
            // Define um valor padrão para sexo caso não seja fornecido
            dependenteExistente.setSexo("Indefinido");  // Exemplo de valor padrão
        }

        // Associa o usuário ao dependente (mantendo a FK do usuário original)
        dependenteExistente.setUsuario_id_fk(usuario);

        // Salva a atualização do dependente
        Dependente dependenteSalvo = dependenteRepository.save(dependenteExistente);

        // Retorna o dependente atualizado
        return ResponseEntity.status(HttpStatus.OK).body(dependenteSalvo);
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
