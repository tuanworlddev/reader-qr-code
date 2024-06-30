package com.tuandev.readerqrcode.controllers;

import com.tuandev.readerqrcode.models.QRCodeData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Excel {
    public Excel() {
    }

    public static void writeExcel(File excelFile, List<QRCodeData> qrCodeDataList) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        try {
            Sheet sheet = workbook.createSheet("QR Code");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Data");

            Font font = workbook.createFont();
            font.setFontName("Calibri");
            CellStyle style = workbook.createCellStyle();
            style.setFont(font);

            for (int i = 0; i < qrCodeDataList.size(); ++i) {
                Row row = sheet.createRow(i + 1);
                Cell cell = row.createCell(0);
                String asciiString = new String(qrCodeDataList.get(i).getValue().getBytes(StandardCharsets.UTF_8), StandardCharsets.US_ASCII);
                System.out.println(asciiString);
                cell.setCellValue(asciiString);
                cell.setCellStyle(style);
            }

            sheet.autoSizeColumn(0);
            try (FileOutputStream outputStream = new FileOutputStream(excelFile)) {
                workbook.write(outputStream);
            }
        } finally {
            workbook.close();
        }
    }

    public static void writeTextFile(File textFile, List<QRCodeData> qrCodeDataList) throws IOException {
        try (FileWriter writer = new FileWriter(textFile)) {
            for (QRCodeData data : qrCodeDataList) {
                writer.write(data.getValue() + "\n");
            }
        }
    }

}
