package com.assoc.file.management.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pattern {
    private String label;
    private String patter;
    private Integer datePosition;
    private Integer namePosition;
}
