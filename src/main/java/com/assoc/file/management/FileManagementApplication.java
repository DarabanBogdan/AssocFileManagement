package com.assoc.file.management;

import com.assoc.file.management.controller.LoginController;
import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;

@SpringBootApplication
@Log
public class FileManagementApplication extends JFrame {
    public static void main(String[] args) {
        SpringApplication.run(FileManagementApplication.class, args);
        LoginController loginController=new LoginController();
        loginController.init();


    }
}
