package com.assoc.file.management.service;

import java.util.List;

public interface FileMover {

     void writeWordOpis(List<String> origin, String destination, String template);
     void writeExcelOpis(List<String> singletonList, String destinationPath, String templatePath);
     List<String> getBeneficiary(String origin);

}
