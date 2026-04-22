package com.fatec.service;

import com.fatec.dto.DependenteDTO;
import com.fatec.dto.InfoJogosDTO;
import com.fatec.model.Dependente;
import com.fatec.model.InfoJogos;
import com.fatec.repository.DependenteRepository;
import com.fatec.repository.InfoJogosRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class RelatorioService {

    private final DependenteRepository dependenteRepository;
    private final InfoJogosRepository infoJogosRepository;
    private final ExcelExportService excelExportService;
    private final PdfExportService pdfExportService;

    public RelatorioService(DependenteRepository dependenteRepository,
                            InfoJogosRepository infoJogosRepository,
                            ExcelExportService excelExportService,
                            PdfExportService pdfExportService) {
        this.dependenteRepository = dependenteRepository;
        this.infoJogosRepository = infoJogosRepository;
        this.excelExportService = excelExportService;
        this.pdfExportService = pdfExportService;
    }

    public byte[] gerarExcel(Long dependenteId) throws IOException {

        Dependente dependente = buscarDependente(dependenteId);
        List<InfoJogosDTO> dados = buscarInfoJogosDTO(dependenteId);

        return excelExportService.exportar(dados, toDTO(dependente));
    }

    public byte[] gerarPdf(Long dependenteId) {

        Dependente dependente = buscarDependente(dependenteId);
        List<InfoJogosDTO> dados = buscarInfoJogosDTO(dependenteId);

        return pdfExportService.exportar(dados, toDTO(dependente));
    }

    private Dependente buscarDependente(Long id) {
        return dependenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dependente não encontrado"));
    }

    private List<InfoJogosDTO> buscarInfoJogosDTO(Long id) {
        return infoJogosRepository.findByDependenteId(id)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private InfoJogosDTO toDTO(InfoJogos e) {
        return new InfoJogosDTO(
                e.getId(),
                e.getTempoTotal(),
                e.getTotalTentativas(),
                e.getTotalAcertos(),
                e.getTotalErros(),
                e.getJogo().getId(),
                e.getDependente().getId(),
                e.getJogo().getNomeJogo(),
                e.getCreateDate(),
                e.getUpdateDate()
        );
    }

    private DependenteDTO toDTO(Dependente e) {
        return new DependenteDTO(
                e.getId(),
                e.getNome(),
                e.getDataNascimento(),
                e.getSexo(),
                e.getFoto()
        );
    }
}
