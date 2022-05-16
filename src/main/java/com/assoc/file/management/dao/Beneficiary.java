package com.assoc.file.management.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Beneficiary {
    private String name;
    private List<BeneficiaryFiles> files;

    public Beneficiary(String name, File f) {
        this.name = name;
        this.files = new ArrayList<>();
        files.add(new BeneficiaryFiles(f));
    }

    public Beneficiary(String name) {
        this.name = name;
        files=new ArrayList<>();
    }

}
