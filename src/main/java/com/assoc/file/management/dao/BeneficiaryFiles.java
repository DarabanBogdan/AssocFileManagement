package com.assoc.file.management.dao;

import com.assoc.file.management.utils.StringComponent;
import lombok.Data;

import java.io.File;

@Data
public class BeneficiaryFiles {

    private int date;
    private File file;
    private FileType type;

    public BeneficiaryFiles(File f) {
        this.date= Integer.parseInt(StringComponent.getWord(f.getName(),1));
        this.file=f;
    }
}
