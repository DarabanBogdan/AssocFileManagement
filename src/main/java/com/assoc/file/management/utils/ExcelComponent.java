package com.assoc.file.management.utils;

import com.assoc.file.management.dao.Beneficiary;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import static com.assoc.file.management.utils.FileComponent.SLASH;

public class ExcelComponent {

    public void addBeneficiaryExcel(String origin, String destination, List<Beneficiary> beneficiaryList) {
        FileInputStream file = null;
        try {
            file = new FileInputStream(origin);

            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);

            int lastRowNumner = sheet.getLastRowNum();

            XSSFRow headerRow = sheet.getRow(2);
            for (int i = 3; i <= lastRowNumner; i++) {
                XSSFRow row = sheet.getRow(i);
                String beneficiaryName = getBeneficiaryName(row);
                //TODO
                Beneficiary bene = beneficiaryList.stream()
                        .filter(b -> b.getName().equals(beneficiaryName))
                        .findFirst()
                        .orElse(null);
                if (bene == null)
                    continue;
                fillRow(headerRow, row, bene);
            }
            FileOutputStream outFile = new FileOutputStream(destination + SLASH + "result.xlsx");
            workbook.write(outFile);
            outFile.close();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillRow(XSSFRow header, XSSFRow row, Beneficiary bene) {
        int maximCellNumber = row.getPhysicalNumberOfCells();
        for (int i = 5; i < maximCellNumber; i++) {
            XSSFCell cell = row.getCell(i);
            XSSFCell headerCell = header.getCell(i);
            //TODO CHECK THIS.
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
        else return string.substring(1);
    }
}
