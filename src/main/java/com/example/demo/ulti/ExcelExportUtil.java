package com.example.demo.ulti;

import com.example.demo.entity.Checker;
import com.example.demo.entity.Student;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class ExcelExportUtil {
    public ByteArrayOutputStream exportCheckerToExcel(List<Checker> dataList) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Họ Tên");
        headerRow.createCell(1).setCellValue("Lớp");
        headerRow.createCell(2).setCellValue("Mã SV");
        headerRow.createCell(3).setCellValue("Ngày");
        headerRow.createCell(4).setCellValue("Giờ vào");
        headerRow.createCell(5).setCellValue("Giờ ra");

        // Populate data rows
        int rowNum = 1;
        for (Checker data : dataList) {
            String timout = data.getTimeout() != null ? data.getTimeout().toString() : "";
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getStudent().getName());
            row.createCell(1).setCellValue(data.getStudent().getClassName());
            row.createCell(2).setCellValue(data.getStudent().getId());
            row.createCell(3).setCellValue(data.getDate().toString());
            row.createCell(4).setCellValue(data.getTimeIn().toString());
            row.createCell(5).setCellValue(timout);
        }

        // Write to ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream;
    }

    public ByteArrayOutputStream exportStudentToExcel(List<Student> dataList) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Họ Tên");
        headerRow.createCell(1).setCellValue("Lớp");
        headerRow.createCell(2).setCellValue("Mã SV");
        headerRow.createCell(3).setCellValue("SDT");
        headerRow.createCell(4).setCellValue("Giới Tính");
        headerRow.createCell(5).setCellValue("Email");

        // Populate data rows
        int rowNum = 1;
        for (Student data : dataList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getName());
            row.createCell(1).setCellValue(data.getClassName());
            row.createCell(2).setCellValue(data.getId());
            row.createCell(3).setCellValue(data.getPhone());
            row.createCell(4).setCellValue(data.getSex());
            row.createCell(5).setCellValue(data.getEmail());
        }

        // Write to ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream;
    }
}
