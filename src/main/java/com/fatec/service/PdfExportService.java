package com.fatec.service;

import com.fatec.dto.DependenteDTO;
import com.fatec.dto.InfoJogosDTO;
import com.fatec.model.Dependente;
import com.fatec.model.InfoJogos;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PdfExportService {

    public byte[] exportar(List<InfoJogosDTO> dados, DependenteDTO dependente) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Relatório de Jogos"));
            document.add(new Paragraph("Dependente: " + dependente.getNome()));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(4);

            table.addCell("Jogo");
            table.addCell("Tempo");
            table.addCell("Acertos");
            table.addCell("Erros");

            for (InfoJogosDTO i : dados) {
                table.addCell(i.getNomeJogo());
                table.addCell(String.valueOf(i.getTempoTotal()));
                table.addCell(String.valueOf(i.getTotalAcertos()));
                table.addCell(String.valueOf(i.getTotalErros()));
            }

            document.add(table);
            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }
}