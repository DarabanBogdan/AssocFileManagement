package com.assoc.file.management.controller;

import com.assoc.file.management.dao.Pattern;
import com.assoc.file.management.dao.PatternList;
import com.assoc.file.management.utils.JsonComponent;
import lombok.extern.java.Log;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import static com.assoc.file.management.validator.Validator.validatePatternCreate;

@Log
public class PatternAddController implements ActionListener {
    private JList<Pattern> jListPattern;
    private JButton addButton;
    private JButton cancelButton;
    private JFrame frame;
    private JPanel jpanel;
    private JTextField labelTextField;
    private JTextField patternTextField;
    private JTextField datePositionTextField;
    private JTextField namePositionTextField;
    private JLabel labelLabel;
    private JLabel patternLabel;
    private JLabel dataPositionLabel;
    private JLabel namePositionLabel;
    private PatternList patternList;

    public PatternAddController(JList<Pattern> jListPattern,PatternList patternList) {
        this.jListPattern = jListPattern;
        this.patternList=patternList;
        show();
    }

    public void show() {
        addButton = new JButton("Add");
        cancelButton = new JButton("Cancel");
        frame = new JFrame("Add pattern");
        frame.setSize(400, 350);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        jpanel = new JPanel();
        labelTextField = new JTextField();
        patternTextField = new JTextField();
        datePositionTextField = new JTextField();
        namePositionTextField = new JTextField();
        labelLabel = new JLabel("Label");
        patternLabel = new JLabel("Pattern");
        dataPositionLabel = new JLabel("Date Position");
        namePositionLabel = new JLabel("Name Position");

        labelLabel.setBounds(10,20,80,20);
        labelTextField.setBounds(110,20,200,20);
        patternLabel.setBounds(10,50,80,20);
        patternTextField.setBounds(110,50,200,20);
        dataPositionLabel.setBounds(10,80,80,20);
        datePositionTextField.setBounds(110,80,200,20);
        namePositionLabel.setBounds(10,110,80,20);
        namePositionTextField.setBounds(110,110,200,20);


        addButton.setBounds(10,150,80,25);
        addButton.addActionListener(this);
        cancelButton.setBounds(100,150,80,25);
        cancelButton.addActionListener(this);

        jpanel.setLayout(null);
        jpanel.add(labelTextField);
        jpanel.add(patternTextField);
        jpanel.add(datePositionTextField);
        jpanel.add(namePositionTextField);
        jpanel.add(labelLabel);
        jpanel.add(patternLabel);
        jpanel.add(dataPositionLabel);
        jpanel.add(namePositionLabel);
        jpanel.add(addButton);
        jpanel.add(cancelButton);
        frame.add(jpanel);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        String com = evt.getActionCommand();

        switch (com) {
            case "Add": {
                try{
                    Pattern pattern=new Pattern(labelTextField.getText(),patternTextField.getText(),Integer.parseInt(datePositionTextField.getText()),Integer.parseInt(namePositionTextField.getText()));
                    validatePatternCreate(pattern);
                    patternList.getPatterns().add(pattern);
                    JsonComponent.writePatterns(patternList);
                    jListPattern.setListData( JsonComponent.readPatterns().getPatterns().toArray(new Pattern[0]));
                    log.info("Pattern added");
                    JOptionPane.showMessageDialog(frame, "Pattern added");
                    frame.dispose();
                }
                catch (Exception e){
                log.info(e.getMessage());
                JOptionPane.showMessageDialog(frame, e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}
                break;
            }
            case "Cancel": {
                frame.dispose();
                break;
            }
            default: {
            }
        }
    }
}
