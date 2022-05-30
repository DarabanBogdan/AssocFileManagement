package com.assoc.file.management.controller;

import com.assoc.file.management.dao.Pattern;
import com.assoc.file.management.dao.PatternList;
import com.assoc.file.management.service.FileMover;
import com.assoc.file.management.service.FileMoverImpl;
import com.assoc.file.management.utils.JsonComponent;
import com.assoc.file.management.validator.Validator;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

@Data
@Service
@Log
public class ApplicationController implements ActionListener {
    private JFrame frame;
    private JTextField destinationTextField;
    private JTextField originTextField;
    private JTextField templateTextField;
    private String destinationPath;
    private String originPath;
    private String templatePath;
    private JPanel panelButtonsLocation;
    private List<String> beneficiaryList;
    private JList<Pattern> jListPattern;
    private List<String> patterns;
    private PatternList patternList;
    public static final String DONE = "Done!";

    public ApplicationController() {
        destinationTextField = new JTextField();
        destinationTextField.setEditable(false);
        originTextField = new JTextField();
        originTextField.setEditable(false);
        templateTextField = new JTextField();
        templateTextField.setEditable(false);
        panelButtonsLocation = new JPanel();
        jListPattern = new JList<>();
    }

    public void show() {
        frame = new JFrame("ASSOC ARM");
        frame.setSize(600, 550);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        initWindow();
        frame.setVisible(true);
    }


    public void initWindow() {
        JButton destination = new JButton("Destination");
        JButton origin = new JButton("Origin");
        JButton template = new JButton("Template");

        JButton move = new JButton("Move");
        JButton opisWord = new JButton("OPIS Word");
        JButton opisExcel = new JButton("OPIS Excel");


        JButton addPattern = new JButton("Add Pattern");
        JButton deletePattern = new JButton("Delete");
        try {
            putPatterList();
        } catch (Exception e) {
            patternList=new PatternList();
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        destination.addActionListener(this);
        destination.setBounds(10, 70, 100, 25);
        destinationTextField.setBounds(129, 70, 450, 25);
        origin.addActionListener(this);
        origin.setBounds(10, 20, 100, 25);
        originTextField.setBounds(129, 20, 450, 25);
        template.addActionListener(this);
        template.setBounds(10, 130, 100, 25);
        templateTextField.setBounds(129, 130, 450, 25);

        move.addActionListener(this);
        move.setBounds(230, 460, 100, 25);
        opisWord.addActionListener(this);
        opisWord.setBounds(350, 460, 100, 25);
        opisExcel.addActionListener(this);
        opisExcel.setBounds(470, 460, 100, 25);

        jListPattern.setBounds(10, 180, 500, 200);
        addPattern.setBounds(10, 400, 100, 25);
        addPattern.addActionListener(this);
        deletePattern.setBounds(120, 400, 100, 25);
        deletePattern.addActionListener(this);

        panelButtonsLocation.add(jListPattern);
        panelButtonsLocation.add(addPattern);
        panelButtonsLocation.add(deletePattern);
        panelButtonsLocation.setLayout(null);
        panelButtonsLocation.add(destination);
        panelButtonsLocation.add(destinationTextField);
        panelButtonsLocation.add(originTextField);
        panelButtonsLocation.add(templateTextField);
        panelButtonsLocation.add(origin);
        panelButtonsLocation.add(template);
        panelButtonsLocation.add(move);
        panelButtonsLocation.add(opisWord);
        panelButtonsLocation.add(opisExcel);

        frame.add(panelButtonsLocation);
    }

    private void putPatterList() throws Exception {
        patternList = JsonComponent.readPatterns();
        jListPattern.setListData( patternList.getPatterns().toArray(new Pattern[0]));
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        String com = evt.getActionCommand();

        switch (com) {
            case "Destination": {
                JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int dialog = fileChooser.showSaveDialog(null);

                if (dialog == JFileChooser.APPROVE_OPTION) {
                    destinationPath = fileChooser.getSelectedFile().getAbsolutePath();
                    destinationTextField.setText(destinationPath);
                }
                break;
            }
            case "Origin": {
                JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int dialog = fileChooser.showSaveDialog(null);
                if (dialog == JFileChooser.APPROVE_OPTION) {
                    originPath = fileChooser.getSelectedFile().getAbsolutePath();
                    originTextField.setText(originPath);
                }
                break;
            }
            case "Template": {
                JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

                int dialog = fileChooser.showSaveDialog(null);
                if (dialog == JFileChooser.APPROVE_OPTION) {
                    templatePath = fileChooser.getSelectedFile().getAbsolutePath();
                    templateTextField.setText(templatePath);
                }
                break;
            }
            case "Move": {
                try {
                    Validator.validatePath(destinationPath, originPath);
                    Validator.validatePattern(jListPattern.getSelectedValue());
                    FileMoverImpl fileMover = new FileMoverImpl();
                    fileMover.moveFiles(Collections.singletonList(originPath), destinationPath,jListPattern.getSelectedValue());
                    log.info(DONE);
                    JOptionPane.showMessageDialog(frame, DONE);
                } catch (Exception e) {
                    log.info(e.getMessage());
                    JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

                }
                break;
            }
            case "OPIS Word": {
                try {
                    Validator.validatePath(destinationPath, originPath, templatePath);
                    Validator.validatePattern(jListPattern.getSelectedValue());
                    FileMover fileMover = new FileMoverImpl();
                    fileMover.writeWordOpis(Collections.singletonList(originPath), destinationPath, templatePath, jListPattern.getSelectedValue());
                    log.info(DONE);
                    JOptionPane.showMessageDialog(frame, DONE);
                } catch (Exception e) {
                    log.info(e.getMessage());
                    JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

                }
                break;
            }
            case "OPIS Excel": {
                try {
                    Validator.validatePath(destinationPath, originPath, templatePath);
                    Validator.validatePattern(jListPattern.getSelectedValue());
                    FileMover fileMover = new FileMoverImpl();
                    fileMover.writeExcelOpis(Collections.singletonList(originPath), destinationPath, templatePath, jListPattern.getSelectedValue());
                    log.info(DONE);
                    JOptionPane.showMessageDialog(frame, DONE);
                } catch (Exception e) {
                    log.info(e.getMessage());
                    JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

                }
                break;
            }
            case "Delete": {
                Pattern pattern = jListPattern.getSelectedValue();
                patternList.getPatterns().remove(pattern);
                JsonComponent.writePatterns(patternList);
                jListPattern.setListData( patternList.getPatterns().toArray(new Pattern[0]));
                break;
            }
            case "Add Pattern": {
                new PatternAddController(jListPattern, patternList);
                break;
            }
            default: {

            }
        }

    }

}
