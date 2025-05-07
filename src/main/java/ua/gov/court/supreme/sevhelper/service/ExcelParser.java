package ua.gov.court.supreme.sevhelper.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelParser {
    public static List<String[]> parseExcel(File excelFile) throws IOException {
        List<String[]> parsedFile = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip the first line with headings
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                String column1 = getCellValue(row.getCell(0));
                String column2 = getCellValue(row.getCell(1));
                String column3 = getCellValue(row.getCell(2));
                String column4 = getCellValue(row.getCell(5));

                parsedFile.add(new String[]{column1, column2, column3, column4});
            }
        }

        return parsedFile;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        return cell.toString().trim();
    }
}
