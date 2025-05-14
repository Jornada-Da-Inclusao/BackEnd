package com.fatec.service;

import com.fatec.model.InfoJogos;
import com.fatec.model.Dependente;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


@Service
public class PdfExportService {
    public void exportDataToPdf(List<InfoJogos> data, Dependente dependente, ByteArrayOutputStream byteArrayOutputStream) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        // Cabeçalho com as informações do dependente
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph header = new Paragraph("Relatório de Dados - Jogos do Dependente", headerFont);
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);
        document.add(Chunk.NEWLINE);

        // Texto Genérico Introduzindo o Relatório
        Font introFont = new Font(Font.FontFamily.HELVETICA, 12);
        Paragraph introText = new Paragraph("Este relatório apresenta informações detalhadas sobre os jogos realizados pelo dependente, incluindo desempenho, tempo total de jogo, tentativas, acertos e erros.", introFont);
        introText.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(introText);
        document.add(Chunk.NEWLINE);

        // Dados do Dependente
        Font infoFont = new Font(Font.FontFamily.HELVETICA, 12);
        document.add(new Paragraph("Nome do Dependente: " + dependente.getNome(), infoFont));
        document.add(new Paragraph("Idade: " + dependente.getIdade(), infoFont));
        document.add(Chunk.NEWLINE);

        // Tabela para os dados de InfoJogos
        PdfPTable table = new PdfPTable(5); // Agora são 6 colunas
        table.setWidthPercentage(100); // Preencher toda a largura da página

        // Cabeçalho da tabela
        table.addCell(new Phrase("Nome do Jogo", infoFont));
        table.addCell(new Phrase("Dificuldade", infoFont));
        table.addCell(new Phrase("Tempo Total", infoFont));
        table.addCell(new Phrase("Tentativas", infoFont));
        table.addCell(new Phrase("Acertos", infoFont));

        // Dados de InfoJogos
        for (InfoJogos entity : data) {
            table.addCell(entity.getInfoJogos_id_fk().getNome()); // Nome do Jogo
            table.addCell(entity.getInfoJogos_id_fk().getDificuldade()); // Dificuldade do Jogo
            table.addCell(String.valueOf(entity.getTempoTotal()));
            table.addCell(String.valueOf(entity.getTentativas()));
            table.addCell(String.valueOf(entity.getAcertos()));
        }

        document.add(table);

        // Fechando o documento
        document.close();
    }


}
