package com.assoc.file.management.controller;

import com.assoc.file.management.service.FileMoverImpl;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

@Data
@Service
public class ApplicationController implements ActionListener {
    private JFrame frame;
    private JLabel destinationLabel;
    private JLabel originLabel;
    private JLabel templateLabel;
    private String destinationPath;
    private String originPath;
    private String templatePath;
    private JPanel panelButtonsLocation;
    private List<String> beneficiaryList;
    private JList<String > jList;

    public ApplicationController() {
        destinationLabel = new JLabel();
        originLabel = new JLabel();
        templateLabel = new JLabel();
        panelButtonsLocation = new JPanel();
        jList=new JList<>();
    }

    public void show() {
        frame = new JFrame("");
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
        JButton opis = new JButton("OPIS");

        destination.addActionListener(this);
        destination.setBounds(10, 20, 100, 25);
        destinationLabel.setBounds(129, 20, 450, 25);
        origin.addActionListener(this);
        origin.setBounds(10, 70, 100, 25);
        originLabel.setBounds(129, 70, 450, 25);
        template.addActionListener(this);
        template.setBounds(10, 130, 100, 25);
        templateLabel.setBounds(129, 130, 450, 25);

        move.addActionListener(this);
        move.setBounds(330, 460, 100, 25);
        opis.addActionListener(this);
        opis.setBounds(450, 460, 100, 25);


        panelButtonsLocation.add(jList);
        panelButtonsLocation.setLayout(null);
        panelButtonsLocation.add(destination);
        panelButtonsLocation.add(destinationLabel);
        panelButtonsLocation.add(originLabel);
        panelButtonsLocation.add(templateLabel);
        panelButtonsLocation.add(origin);
        panelButtonsLocation.add(template);
        panelButtonsLocation.add(move);
        panelButtonsLocation.add(opis);

        frame.add(panelButtonsLocation);
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
                    destinationLabel.setText(destinationPath);
                }
                break;
            }
            case "Origin": {
                JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int dialog = fileChooser.showSaveDialog(null);
                if (dialog == JFileChooser.APPROVE_OPTION) {
                    originPath = fileChooser.getSelectedFile().getAbsolutePath();
                    originLabel.setText(originPath);
                }
                break;
            }
            case "Template": {
                JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

                int dialog = fileChooser.showSaveDialog(null);
                if (dialog == JFileChooser.APPROVE_OPTION) {
                    templatePath = fileChooser.getSelectedFile().getAbsolutePath();
                    templateLabel.setText(templatePath);
                }
                break;
            }
            case "Move": {
                if (destinationPath.isEmpty() || originPath.isEmpty())
                    Toast.getErrorToast("Missing path!").setVisible(true);
                else {
                    FileMoverImpl fileMover = new FileMoverImpl();
                    fileMover.functionalityTwo(Collections.singletonList(originPath), destinationPath);
                    Toast.getSuccesToast();
                }
                break;
            }
            case "OPIS": {
                if (destinationPath.isEmpty() || originPath.isEmpty())
                    Toast.getErrorToast("Missing path!").setVisible(true);
                else {
                    FileMoverImpl fileMover = new FileMoverImpl();
                    fileMover.functionalityOne(Collections.singletonList(originPath), destinationPath, templatePath);
                    Toast.getSuccesToast();
                }
                break;
            }

            default: {

            }
        }

    }

}
