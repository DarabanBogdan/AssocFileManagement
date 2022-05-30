package com.assoc.file.management.dao;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PatternList {
    private String name;
    private List<Pattern> patterns;

    public PatternList(){
        patterns=new ArrayList<>();
    }
}
