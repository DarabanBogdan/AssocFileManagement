package com.assoc.file.management.service;

import com.assoc.file.management.controller.Toast;
import com.assoc.file.management.dao.Beneficiary;
import com.assoc.file.management.dao.BeneficiaryFiles;
import com.assoc.file.management.utils.ExcelComponent;
import com.assoc.file.management.utils.FileComponent;
import com.assoc.file.management.utils.WordComponent;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

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
    private List<Beneficiary> listBene;

    public FileMoverImpl() {
        listBene = new ArrayList<>();
    }

    public void functionalityTwo(List<String> origin, String destination) {
        List<File> fileList = new ArrayList<>();
        log.info("Reading files from:" + origin);
        for (String fr : origin) {
            File originDirectory = new File(fr);
            fileList.addAll(findAllFiles(originDirectory));
        }
        log.info("Read " + fileList.size() + "files.");
        String pattern = ".{1,255}[_][a-zA-Z]{1,255}[_].{1,255}";
        fileList.forEach(f -> {
                    if (patternCheck(f, pattern))
                        moveFileToUser(destination, getWord(f.getName(), 1), f);
                    else
                        log.warning("File:" + f.getName() + " does not match the pattern.");
                }
        );
    }
    public void writeWordOpis(List<String> origin, String destination, String template) {
        List<File> fileList = new ArrayList<>();
        log.info("Reading files from:" + origin);
        for (String fr : origin) {
            File originDirectory = new File(fr);
            fileList.addAll(findAllFiles(originDirectory));
        }
        String pattern = ".{1,255}[_][0-9]{6}[_][a-zA-Z]{1,255}[_].{1,255}";
        fileList.forEach(f -> {
                    if (patternCheck(f, pattern))
                        createListBeneficiary(getWord(f.getName(), 2), f,1);
                    else
                        log.warning("File:" + f.getName() + " does not match the pattern.");
                }
        );

        listBene.forEach(l -> createOpis(l, destination, template));
    }

    private void createOpis(Beneficiary beneficiary, String destination, String template) {
        try {
            log.info("Write OPIS for:" + beneficiary.getName());
            writeOpis(beneficiary.getFiles(), destination + SLASH + beneficiary.getName(), template, getSplitName(beneficiary.getName()));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.getErrorToast(e.getMessage());
        }
    }

    private void createListBeneficiary(String name, File f, int position) {
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
        beneficiary.forEach(b->{
            StringJoiner sj=new StringJoiner("_");
            sj.add(date);
            sj.add(b);
            FileComponent.saveCopy(origin, destination, sj.toString());
        });
    }

    public void writeExcelOpis(List<String> origin, String destinationPath, String templatePath) {
        List<File> fileList = new ArrayList<>();
        log.info("Reading files from:" + origin);
        for (String fr : origin) {
            File originDirectory = new File(fr);
            fileList.addAll(findAllFiles(originDirectory));
        }
        String pattern = "[0-9]{6}[_][a-zA-Z]{1,255}[_].{1,255}";
        fileList.forEach(f -> {
                    if (patternCheck(f, pattern))
                        createListBeneficiary(getWord(f.getName(), 1), f,0);
                    else
                        log.warning("File:" + f.getName() + " does not match the pattern.");
                }
        );

        ExcelComponent excelComponent=new ExcelComponent();
        excelComponent.addBeneficiaryExcel(templatePath,destinationPath,listBene);
    }
}
