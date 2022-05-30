package com.assoc.file.management.utils;

import com.assoc.file.management.dao.PatternList;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;

import java.io.File;
import java.io.IOException;

import static com.assoc.file.management.utils.FileComponent.getProgramPath;

@Log
public class JsonComponent {
    public static void writePatterns(PatternList list) {
        log.info("Writing pattern file.");
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(getProgramPath(), list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Writing done.");
    }

    public static PatternList readPatterns() throws Exception {
        log.info("Reading patterns.");
        File patternFile=getProgramPath();
        if (!patternFile.exists()) {
            throw new Exception("No pattern File!");
        }
        ObjectMapper mapper = new ObjectMapper();
        PatternList patternList= null;
        try {
            patternList = mapper.readValue(patternFile, PatternList.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patternList;
    }
}
