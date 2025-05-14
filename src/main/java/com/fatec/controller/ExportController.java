//package com.fatec.controller;
//
//import com.fatec.model.InfoJogos;
//import com.fatec.service.ExcelExportService;
//import com.fatec.service.PdfExportService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/export")
//public class ExportController {
//
//    @Autowired
//    private ExcelExportService excelExportService;
//
//    @Autowired
//    private PdfExportService pdfExportService;
//
//    // Endpoint para exportar para Excel
//    @GetMapping("/excel")
//    public ResponseEntity<byte[]> exportToExcel() throws IOException {
//        // Aqui você pode pegar os dados do banco de dados
//        List<InfoJogos> data = getDataFromDatabase();
//
//        // Gerando o Excel
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        excelExportService.exportDataToExcel(data);  // Ajuste para exportar diretamente para o byte stream, se necessário
//
//        // Convertendo para byte array para envio no ResponseEntity
//        byte[] excelData = byteArrayOutputStream.toByteArray();
//
//        // Definindo o cabeçalho para download do arquivo Excel
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio.xlsx");
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .body(excelData);
//    }
//
//    // Endpoint para exportar para PDF
//    @GetMapping("/pdf")
//    public ResponseEntity<byte[]> exportToPdf() throws IOException {
//        // Aqui você pode pegar os dados do banco de dados
//        List<InfoJogos> data = getDataFromDatabase();
//
//        // Gerando o PDF
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        pdfExportService.exportDataToPdf(data);  // Ajuste para exportar diretamente para o byte stream, se necessário
//
//        // Convertendo para byte array para envio no ResponseEntity
//        byte[] pdfData = byteArrayOutputStream.toByteArray();
//
//        // Definindo o cabeçalho para download do arquivo PDF
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio.pdf");
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .body(pdfData);
//    }
//
//        // Simulação de método para obter os dados do banco (você pode usar JPA ou JDBC aqui)
//        private List<InfoJogos> getDataFromDatabase() {
//            // Aqui você faz a consulta no banco para pegar os dados da tabela
//            // Exemplo de uma consulta simples
//            // return myEntityRepository.findAll();
//
//            // Apenas um exemplo, substitua pela lógica real para pegar os dados
//            return List.of(new InfoJogos(1, "Nome 1", 100), new InfoJogos(2, "Nome 2", 200));
//        }
//}
