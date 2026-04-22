package com.fatec.service;

import com.fatec.dto.DependenteDTO;
import com.fatec.dto.InfoJogosDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {

    public byte[] exportar(List<InfoJogosDTO> dados, DependenteDTO dependente) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Relatório");

        // Cabeçalho
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Dependente");
        header.createCell(1).setCellValue("Jogo");
        header.createCell(2).setCellValue("Tempo");
        header.createCell(3).setCellValue("Tentativas");
        header.createCell(4).setCellValue("Acertos");
        header.createCell(5).setCellValue("Erros");

        int rowNum = 1;

        for (InfoJogosDTO i : dados) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(dependente.getNome());
            row.createCell(1).setCellValue(i.getNomeJogo());
            row.createCell(2).setCellValue(i.getTempoTotal());
            row.createCell(3).setCellValue(i.getTotalTentativas());
            row.createCell(4).setCellValue(i.getTotalAcertos());
            row.createCell(5).setCellValue(i.getTotalErros());
        }

        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }
}