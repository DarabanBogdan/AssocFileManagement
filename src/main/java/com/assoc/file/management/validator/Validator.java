package com.assoc.file.management.validator;

import com.assoc.file.management.dao.Pattern;

public class Validator {
    public static final String MISSING_PATH = "Missing path!";
    public static final String MISSING_PATTERN = "Missing PATTERN!";

    public static void validatePath(String s1, String s2, String s3) throws Exception {
        if (s1.isEmpty() || s2.isEmpty() || s3.isEmpty()) {
            throw new Exception(MISSING_PATH);
        }
    }

    public static void validatePath(String s1, String s2) throws Exception{
        if (s1.isEmpty() || s2.isEmpty()) {
            throw new Exception(MISSING_PATH);
        }
    }

    public static void validatePattern(Pattern s1) throws Exception{
        if (s1 == null || s1.getPatter().isEmpty()) {
             throw new Exception(MISSING_PATTERN);
        }
    }
    public static void validatePatternCreate(Pattern s1) throws Exception{
        if (s1.getLabel().isEmpty()){
            throw new Exception("Missing label.");
        }
        if(s1.getPatter().isEmpty()){
            throw new Exception("Missing pattern.");
        }
        if(s1.getDatePosition()==null){
            throw new Exception("Missing date position.");
        }
        if(s1.getNamePosition()==null){
            throw new Exception("Missing name position.");
        }

    }
}
