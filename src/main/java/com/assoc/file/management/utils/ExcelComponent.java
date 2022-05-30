package com.assoc.file.management.utils;

import com.assoc.file.management.dao.Beneficiary;
import lombok.extern.java.Log;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import static com.assoc.file.management.utils.FileComponent.SLASH;

@Log
public class ExcelComponent {
    int count = 0;

    public void addBeneficiaryExcel(String origin, String destination, List<Beneficiary> beneficiaryList) {
        FileInputStream file = null;
        FileInputStream fileCount = null;
        try {
            file = new FileInputStream(origin);
            fileCount = new FileInputStream(origin);
            log.info("Writing excel.");
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFWorkbook workbookCount = new XSSFWorkbook(fileCount);
            XSSFSheet sheetCount = workbookCount.getSheetAt(0);
            XSSFSheet sheet = workbook.getSheetAt(0);


            int lastRowNumner = sheet.getLastRowNum();

            XSSFRow headerRow = sheet.getRow(2);
            XSSFRow headerRowCount = sheet.getRow(2);
            for (int i = 3; i <= lastRowNumner; i++) {
                XSSFRow row = sheet.getRow(i);
                XSSFRow rowCount = sheetCount.getRow(i);
                String beneficiaryName = getBeneficiaryName(row);
                Beneficiary bene = beneficiaryList.stream()
                        .filter(b -> b.getName().equals(beneficiaryName))
                        .findFirst()
                        .orElse(null);
                if (bene == null) {
                    log.info("Beneficiary " + beneficiaryName + "does not exist!");
                    continue;
                }
                fillRow(headerRow, row, bene);
                fillRowCount(headerRowCount, rowCount, bene);
            }
            FileOutputStream outFile = new FileOutputStream(destination + SLASH + "result.xlsx");
            FileOutputStream outFileCount = new FileOutputStream(destination + SLASH + "resultCount.xlsx");
            workbook.write(outFile);
            workbookCount.write(outFileCount);
            log.info("Wrote " + count + " rows in excel");
            outFile.close();
            outFileCount.close();
            outFile.close();
            fileCount.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillRowCount(XSSFRow headerRow, XSSFRow rowCount, Beneficiary bene) {
        int maximCellNumber = rowCount.getPhysicalNumberOfCells();
        for (int i = 5; i < maximCellNumber; i++) {
            XSSFCell cell = rowCount.getCell(i);
            cell.setBlank();
            XSSFCell headerCell = headerRow.getCell(i);
            String[] typeCell = headerCell.getStringCellValue().split(",");
            Arrays.stream(typeCell).forEach(type -> {
                String value = cell.getStringCellValue();
                int valueCell = 0;
                if (value!=null && !value.isEmpty())
                    valueCell = Integer.parseInt(value);
                cell.setBlank();
                valueCell+=bene.getFilesByType(type).size();
                cell.setCellValue(Integer.toString(valueCell));
            });
        }
    }

    private void fillRow(XSSFRow header, XSSFRow row, Beneficiary bene) {
        count++;
        int maximCellNumber = row.getPhysicalNumberOfCells();
        log.info("Write row for:" + bene.getName() + "with " + bene.getFiles().size() + " files");
        for (int i = 5; i < maximCellNumber; i++) {
            XSSFCell cell = row.getCell(i);
            XSSFCell headerCell = header.getCell(i);
            String[] typeCell = headerCell.getStringCellValue().split(",");
            Arrays.stream(typeCell).forEach(type -> writeInCell(cell, bene.getFilesByType(type)));
        }
    }

    private void writeInCell(XSSFCell cell, List<String> filesName) {
        StringJoiner sj = new StringJoiner("\n");
        String alreadyText = cell.getStringCellValue();
        if (!alreadyText.isEmpty())
            sj.add(alreadyText);
        filesName.forEach(f -> sj.add(StringComponent.removeExtension(f)));
        cell.setCellValue(sj.toString());
    }

    private String getBeneficiaryName(XSSFRow row) {
        String string = row.getCell(4).getStringCellValue();
        if (string.isEmpty())
            return null;
        else return string.substring(1, string.length() - 1);
    }
}
