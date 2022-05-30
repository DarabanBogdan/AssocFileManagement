package com.assoc.file.management.controller;

import com.assoc.file.management.service.UserService;
import com.assoc.file.management.dao.User;
import com.assoc.file.management.service.UserServiceImpl;
import lombok.Data;
import lombok.extern.java.Log;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


@Data
@Log
public class LoginController {
    private  JFrame frame;
    private UserService userService;
    private  JTextField usernameTextField;
    private  JPasswordField passwordTextField;

    public  void init() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        frame = new JFrame("Assoc ARM");
        userService = new UserServiceImpl(new User("drb", "123"));
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(initPanel());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private  JPanel initPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(10, 20, 80, 25);
        panel.add(usernameLabel);
        usernameTextField = new JTextField(20);
        usernameTextField.setBounds(100, 20, 165, 25);
        panel.add(usernameTextField);
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);
        passwordTextField = new JPasswordField(20);
        passwordTextField.setBounds(100, 50, 165, 25);
        panel.add(passwordTextField);
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 100, 100, 25);
        loginButton.addActionListener(new ButtonAction());

        panel.add(loginButton);
        return panel;

    }

    public  class ButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            log.info("Attempt logging.");
            if (userService.Auth(getUser())) {
                frame.setVisible(false);
                frame.dispose();
                ApplicationController applicationController=new ApplicationController();
                applicationController.show();
            } else
                JOptionPane.showMessageDialog(frame, "Username or password invalid!!","Error",JOptionPane.ERROR_MESSAGE);
        }

        private User getUser() {
            return new User(usernameTextField.getText(), new String(passwordTextField.getPassword()));
        }
    }
}
