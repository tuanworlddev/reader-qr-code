package com.tuandev.readerqrcode.controllers;

import com.tuandev.readerqrcode.models.QRCodeData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

            for(int i = 0; i < qrCodeDataList.size(); ++i) {
                Row row = sheet.createRow(i + 1);
                Cell cell = row.createCell(0);
                cell.setCellValue(((QRCodeData)qrCodeDataList.get(i)).getValue());
            }

            sheet.autoSizeColumn(0);
            FileOutputStream outputStream = new FileOutputStream(excelFile);

            try {
                workbook.write(outputStream);
            } catch (Throwable var10) {
                try {
                    outputStream.close();
                } catch (Throwable var9) {
                    var10.addSuppressed(var9);
                }

                throw var10;
            }

            outputStream.close();
        } catch (Throwable var11) {
            try {
                workbook.close();
            } catch (Throwable var8) {
                var11.addSuppressed(var8);
            }

            throw var11;
        }

        workbook.close();
    }
}
