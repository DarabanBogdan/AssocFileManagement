package com.assoc.file.management.utils;

import com.assoc.file.management.dao.Beneficiary;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import javax.print.DocFlavor;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
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

    public static void moveFileToUser(String destination, List<Beneficiary> beneficiaries) {

        List<File> fileList = List.of(Objects.requireNonNull(new File(destination).listFiles()));
        Map<String, File> map = new HashMap<>();
        Map<String, File> mapUndefined = new HashMap<>();
        for (File file : fileList) {
            if (file.isDirectory() && !StringComponent.getNP(file.getName()).equals("Error")) {
                String np = StringComponent.getNP(file.getName());
                if (mapUndefined.containsKey(np)) {
                    mapUndefined.put(np, file);
                    continue;
                }
                if (map.containsKey(np)) {
                    mapUndefined.put(np, file);
                    mapUndefined.put(np, map.get(np));
                    map.remove(np);
                    continue;
                }
                map.put(np, file);
            }
        }
        File undefined=new File(destination+SLASH+"Undefined");
        if(mapUndefined.size()>0){
            undefined.mkdirs();
        }

        beneficiaries.forEach(b -> {
            String name= b.getName();
            if (map.containsKey(name)) {
                b.getFiles().forEach(f -> {
                    try {
                        Files.copy(f.getFile().toPath(), Path.of(map.get(name).toPath() + SLASH + f.getFile().getName()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                    if(!undefined.exists())
                        undefined.mkdirs();
                    b.getFiles().forEach(f -> {
                        try {
                            Files.copy(f.getFile().toPath(), Path.of(undefined.getAbsolutePath() + SLASH + f.getFile().getName()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            }
        });

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

