package com.assoc.file.management.dao;

import com.assoc.file.management.utils.StringComponent;
import lombok.Data;

import java.io.File;

import static com.assoc.file.management.utils.StringComponent.UNDER_SCORE;

@Data
public class BeneficiaryFiles {

    private int date;
    private File file;
    private String type;

    public BeneficiaryFiles(File f,int position) {
        this.date= Integer.parseInt(StringComponent.getWord(f.getName(),position));
        this.file=f;
        type=UNDER_SCORE+StringComponent.removeExtension(StringComponent.getLastWord(f.getName()));
    }
}
