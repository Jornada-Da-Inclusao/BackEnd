package com.fatec.service;

import com.fatec.model.Dependente;
import com.fatec.model.InfoJogos;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


@Service
public class ExcelExportService {
    public void exportDataToExcel(List<InfoJogos> data, Dependente dependente, ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Relatório");

        // Cabeçalho
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Nome do Dependente");
        headerRow.createCell(1).setCellValue("Idade do Dependente");
        headerRow.createCell(2).setCellValue("Nome do Jogo");
        headerRow.createCell(3).setCellValue("Dificuldade do Jogo");
        headerRow.createCell(4).setCellValue("Tempo Total");
        headerRow.createCell(5).setCellValue("Tentativas");
        headerRow.createCell(6).setCellValue("Acertos");
        headerRow.createCell(7).setCellValue("Erros");

        // Preenchendo as informações do dependente na segunda linha
        Row dependentRow = sheet.createRow(1);
        dependentRow.createCell(0).setCellValue(dependente.getNome());
        dependentRow.createCell(1).setCellValue(dependente.getIdade());

        // Dados de InfoJogos começam na linha 2
        int rowNum = 2;
        for (InfoJogos entity : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(2).setCellValue(entity.getInfoJogos_id_fk().getNome());
            row.createCell(3).setCellValue(entity.getInfoJogos_id_fk().getDificuldade());
            row.createCell(4).setCellValue(entity.getTempoTotal());
            row.createCell(5).setCellValue(entity.getTentativas());
            row.createCell(6).setCellValue(entity.getAcertos());
            row.createCell(7).setCellValue(entity.getErros());
        }

        // Escrevendo os dados no ByteArrayOutputStream
        workbook.write(byteArrayOutputStream);
        workbook.close();
    }

}
