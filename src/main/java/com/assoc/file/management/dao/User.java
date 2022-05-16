package com.assoc.file.management.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    private String username;
    private String password;
}
