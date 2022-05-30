package com.assoc.file.management.utils;

import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Component
@Log
public class FileComponent {
    public static final String SLASH = "\\";
    public static final String DOT_PDF = ".pdf";

    private FileComponent() {
    }

    public static File getProgramPath() {
        String currentdir = System.getProperty("user.dir");
        currentdir = currentdir.replace("\\", "/");
        log.info(currentdir);
        return new File(currentdir + SLASH + "PatternFile.json");
    }

    public static void moveFileToUser(String destination, String name, File file) {
        log.info("Moving file:" + file.getName() + "to location:" + destination + SLASH + name);
        if (name != null) {
            File destinationFile;
            File directory = new File(destination + SLASH + name);
            if (!directory.exists()) {
                    directory.mkdirs();
            }
            destinationFile = new File(destination + SLASH + name + SLASH + file.getName());

//            try {
//                destinationFile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            try {
                Files.copy(file.toPath(), destinationFile.toPath());
            } catch (IOException e) {
                log.info(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static boolean patternCheck(File file, String pattern) {
        return file.getName().matches(pattern);
    }

    public static List<File> findAllFiles(File path) {
        List<File> fileList = List.of(Objects.requireNonNull(path.listFiles()));
        List<File> list = new ArrayList<>();
        for (File file : fileList) {
            if (file.isFile())
                list.add(file);
            else
                list.addAll(findAllFiles(file));
        }
        return list;
    }

    public static List<File> findAllFoldersOneDeepLevel(File origin) {
        List<File> fileList = List.of(Objects.requireNonNull(origin.listFiles()));
        return fileList.stream()
                .filter(File::isDirectory)
                .collect(Collectors.toList());
    }

    public static void saveCopy(String origin, String destination, String newName) {
        log.info("Creating file:" + newName + "to location:" + destination);
        try {
            Files.copy(new File(origin).toPath(), new File(destination + SLASH + newName + DOT_PDF).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

