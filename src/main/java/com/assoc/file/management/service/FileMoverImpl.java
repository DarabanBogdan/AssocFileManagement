package com.assoc.file.management.service;

import com.assoc.file.management.dao.Beneficiary;
import com.assoc.file.management.dao.BeneficiaryFiles;
import com.assoc.file.management.dao.Pattern;
import com.assoc.file.management.utils.ExcelComponent;
import com.assoc.file.management.utils.FileComponent;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static com.assoc.file.management.utils.FileComponent.*;
import static com.assoc.file.management.utils.StringComponent.getSplitName;
import static com.assoc.file.management.utils.StringComponent.getWord;
import static com.assoc.file.management.utils.WordComponent.writeOpis;


@Log
@Service
@Data
public class FileMoverImpl implements FileMover {

    public FileMoverImpl() {
    }

    @Override
    public void moveFiles(List<String> origin, String destination, Pattern pattern) {
        List<File> fileList = new ArrayList<>();
        log.info("Reading files from:" + origin);
        for (String fr : origin) {
            File originDirectory = new File(fr);
            fileList.addAll(findAllFiles(originDirectory));
        }
        log.info("Read " + fileList.size() + "files.");
        // String pattern = ".{1,255}[_][a-zA-Z]{1,255}[_].{1,255}";
        fileList.forEach(f -> {
                    if (patternCheck(f, pattern.getPatter()))
                        moveFileToUser(destination, getWord(f.getName(), pattern.getNamePosition()), f);
                    else
                        log.warning("File:" + f.getName() + " does not match the pattern.");
                }
        );
    }

    @Override
    public void writeWordOpis(List<String> origin, String destination, String template, Pattern pattern) throws IOException {
        log.info("Reading files from:" + origin);
        List<File> fileList = readAllFiles(origin);

        // String pattern = ".{1,255}[_][0-9]{6}[_][a-zA-Z]{1,255}[_].{1,255}";
        List<Beneficiary> listBene = createListBeneficiary(fileList, pattern);

        for (Beneficiary l : listBene) {
            createOpis(l, destination, template);
        }
    }

    private void createOpis(Beneficiary beneficiary, String destination, String template) throws IOException {
        log.info("Write OPIS for:" + beneficiary.getName());
        writeOpis(beneficiary.getFiles(), destination + SLASH + beneficiary.getName(), template, getSplitName(beneficiary.getName()));
    }

    private void addBeneficiary(List<Beneficiary> listBene, String name, File f, int position) {
        Optional<Beneficiary> bene = listBene.stream()
                .filter(b -> b.getName().equals(name))
                .findFirst();
        if (bene.isEmpty())
            listBene.add(new Beneficiary(name, f, position));
        else
            bene.get().getFiles().add(new BeneficiaryFiles(f, position));
    }


    public List<String> getBeneficiary(String origin) {
        List<File> fileList = findAllFoldersOneDeepLevel(new File(origin));
        return fileList.stream().map(File::getName).collect(Collectors.toList());

    }

    public void functionalityThree(String origin, String destination, List<String> beneficiary) {
        String date = getWord(origin, 0);
        beneficiary.forEach(b -> {
            StringJoiner sj = new StringJoiner("_");
            sj.add(date);
            sj.add(b);
            FileComponent.saveCopy(origin, destination, sj.toString());
        });
    }

    @Override
    public void writeExcelOpis(List<String> origin, String destinationPath, String templatePath, Pattern pattern) {
        log.info("Reading files from:" + origin);
        List<File> fileList = readAllFiles(origin);
        // String pattern = "[0-9]{6}[_][a-zA-Z[-]0-9]{1,255}[_].{1,255}";
        log.info("Create Beneficiary List");
        List<Beneficiary> listBene = createListBeneficiary(fileList, pattern);
        log.info("List created with " + listBene.size() + " beneficiaries");
        listBene.forEach(b -> b.setFiles(b.getFiles().stream().sorted((o1, o2) -> Integer.compare(o2.getDate(), o1.getDate())).collect(Collectors.toList())));
        ExcelComponent excelComponent = new ExcelComponent();
        excelComponent.addBeneficiaryExcel(templatePath, destinationPath, listBene);
    }

    private List<File> readAllFiles(List<String> origin) {
        List<File> fileList = new ArrayList<>();
        for (String fr : origin) {
            File originDirectory = new File(fr);
            fileList.addAll(findAllFiles(originDirectory));
        }
        return fileList;
    }

    private List<Beneficiary> createListBeneficiary(List<File> fileList, Pattern pattern) {
        List<Beneficiary> listBene = new ArrayList<>();
        fileList.forEach(f -> {
                    if (patternCheck(f, pattern.getPatter())) {
                        addBeneficiary(listBene, getWord(f.getName(), pattern.getNamePosition()), f, 0);
                    }
                }
        );
        return listBene;
    }
}
