package com.seds519.hw1.parser;

import com.seds519.hw1.model.Course;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component("excel")
public class ExcelFileParser implements FileParser {
    private static final Map<Integer, String> COLUMN_TO_DAY = Map.of(
            1, "MONDAY",
            4, "TUESDAY",
            7, "WEDNESDAY",
            10, "THURSDAY",
            13, "FRIDAY"
    );

    @Override
    public List<Course> parse(MultipartFile file) throws Exception {
        List<Course> courses = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0); // Monday, Tuesday...

            int rowIndex = 0;
            String currentTimeSlot = "08:45-09:30"; // İlk saat varsayılan olarak
            while (rowIndex <= sheet.getLastRowNum()) {
                Row timeRow = sheet.getRow(rowIndex);
                if (timeRow == null) {
                    rowIndex++;
                    continue;
                }

                Cell timeCell = timeRow.getCell(0); // A sütunu
                String possibleTime = getCellValue(timeCell).trim();

                // Eğer bu hücre saat formatına uyuyorsa timeSlot olarak al
                if (!possibleTime.isBlank() && possibleTime.matches("\\d{2}:\\d{2}-\\d{2}:\\d{2}")) {
                    currentTimeSlot = possibleTime;
                }

                // Diğer satırlar ders içeren satırlar
                Row dataRow = sheet.getRow(rowIndex);

                for (int col = 1; col < dataRow.getLastCellNum(); col += 3) {
                    String classroom = getCellValue(dataRow.getCell(col));
                    String courseName = getCellValue(dataRow.getCell(col + 1));
                    String instructor = getCellValue(dataRow.getCell(col + 2));
                    //    String day = getCellValue(headerRow.getCell(col));
                    String day = COLUMN_TO_DAY.getOrDefault(col, "");

                    if (!courseName.isBlank()) {
                        courses.add(new Course(day, courseName, currentTimeSlot, instructor, classroom));
                    }

                }

                rowIndex++;
            }


        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file", e);
        }

        return courses;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

}