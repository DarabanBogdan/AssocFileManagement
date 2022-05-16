package com.assoc.file.management.utils;

import com.assoc.file.management.dao.BeneficiaryFiles;
import lombok.extern.java.Log;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Log
public class WordComponent {

    private static final String GRUP_TINTA="GRUP ȚINTĂ /";
    private static final String WHITE_SPACE=" ";

    private WordComponent(){

    }

    public static void wordInsertTableRow(String destination, List<File> listFiles, String template, String beneficiaryName) throws IOException {

        XWPFDocument  doc = new XWPFDocument(new FileInputStream(template));
        XWPFTable table = doc.getTableArray(0);
        List<XWPFParagraph> pr=doc.getParagraphs();
        pr.forEach(f->f.getRuns()
                .stream()
                .filter(r->r.getText(0).equals(GRUP_TINTA))
                .findFirst()
                .ifPresent(m->m.setText(GRUP_TINTA+WHITE_SPACE+beneficiaryName)));

        listFiles.forEach(file -> {
            XWPFTableRow lastRow = table.getRows().get(table.getNumberOfRows() - 1);
            CTRow ctrow = null;
            try {
                ctrow = CTRow.Factory.parse(lastRow.getCtRow().newInputStream());
            } catch (XmlException | IOException e) {
                e.printStackTrace();
            }
            XWPFTableRow newRow = new XWPFTableRow(ctrow, table);
            XWPFTableCell cell = newRow.getCell(1);
            XWPFParagraph paragraph = cell.getParagraphArray(0);
            XWPFRun run = paragraph.getRuns().get(0);
            run.setText(file.getName(), 0);
            table.addRow(newRow);
        });
        table.removeRow(0);


        doc.write(new FileOutputStream(destination + FileComponent.SLASH + "result.docx"));
        doc.close();

    }

    public static void writeOpis(List<BeneficiaryFiles> files, String destination, String template,String beneficiaryName) throws IOException {
        files = files.stream()
                .sorted((o1, o2) -> Integer.compare(o2.getDate(), o1.getDate()))
                .collect(Collectors.toList());
        wordInsertTableRow(destination, files.stream()
                .map(BeneficiaryFiles::getFile)
                .collect(Collectors.toList()), template,beneficiaryName);
    }
}
