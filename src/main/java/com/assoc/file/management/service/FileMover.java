package com.assoc.file.management.service;

import com.assoc.file.management.dao.Pattern;

import java.io.IOException;
import java.util.List;

public interface FileMover {

     void moveFiles(List<String> origin, String destination, Pattern pattern);

     void writeWordOpis(List<String> origin, String destination, String template, Pattern pattern) throws IOException;
     void writeExcelOpis(List<String> singletonList, String destinationPath, String templatePath, Pattern pattern) throws IOException;
     List<String> getBeneficiary(String origin);

}
