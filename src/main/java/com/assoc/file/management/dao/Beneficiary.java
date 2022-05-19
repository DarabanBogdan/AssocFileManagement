package com.assoc.file.management.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class Beneficiary {
    private String name;
    private List<BeneficiaryFiles> files;

    public Beneficiary(String name, File f, int position) {
        this.name = name;
        this.files = new ArrayList<>();
        files.add(new BeneficiaryFiles(f, position));
    }

    public Beneficiary(String name) {
        this.name = name;
        files=new ArrayList<>();
    }

    public List<String> getFilesByType(String type) {
        return files.stream()
                .filter(f->f.getType().equals(type))
                .map(f->f.getFile().getName())
                .collect(Collectors.toList());
    }
}
