package com.ems.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Home.java
 * Entry point UI for the Employee Management System.
 * Provides navigation to all major features: View, Add, Delete, Update Employees.
 *
 * Author: Darshan Kumar GP
 * Project: EMS (Employee Management System)
 */
public class Home extends JFrame {

    public Home() {
        // Window Settings
        setTitle("EMS - Employee Management System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center on screen
        setLayout(null);
        setResizable(false);

        // App Logo
        setIconImage(new ImageIcon("images/ems_logo.png").getImage());

        // Title Label
        JLabel title = new JLabel("EMPLOYEE MANAGEMENT SYSTEM", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(80, 30, 440, 40);
        title.setOpaque(true);
        title.setBackground(Color.DARK_GRAY);
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(title);

        // Main Panel for Buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 15, 15));
        panel.setBounds(150, 100, 300, 250);
        panel.setBackground(Color.BLACK);

        // Button Config
        Font btnFont = new Font("Arial", Font.BOLD, 16);
        Color btnBg = new Color(30, 144, 255); // DodgerBlue
        Color btnFg = Color.WHITE;

        // Buttons
        JButton viewAllBtn = new JButton("VIEW ALL EMPLOYEES");
        JButton viewBtn = new JButton("VIEW EMPLOYEE");
        JButton addBtn = new JButton("ADD EMPLOYEE");
        JButton deleteBtn = new JButton("DELETE EMPLOYEE");
        JButton updateBtn = new JButton("UPDATE EMPLOYEE");

        // Apply styles + tooltips
        JButton[] buttons = { viewAllBtn, viewBtn, addBtn, deleteBtn, updateBtn };
        String[] tips = {
            "View list of all employees",
            "Search and view an employee by ID",
            "Add a new employee to the system",
            "Delete an existing employee",
            "Update employee details"
        };

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setFont(btnFont);
            buttons[i].setBackground(btnBg);
            buttons[i].setForeground(btnFg);
            buttons[i].setFocusPainted(false);
            buttons[i].setToolTipText(tips[i]);
            panel.add(buttons[i]);
        }

        add(panel);

        // Navigation Actions
        viewAllBtn.addActionListener(e -> {
            new DisplayAll().setVisible(true);
            dispose();
        });

        viewBtn.addActionListener(e -> {
            new SingleEmployee().setVisible(true);
            dispose();
        });

        addBtn.addActionListener(e -> {
            new OnboardEmployee().setVisible(true);
            dispose();
        });

        deleteBtn.addActionListener(e -> {
            new DeboardEmployee().setVisible(true);
            dispose();
        });

        updateBtn.addActionListener(e -> {
            new UpdateEmployee().setVisible(true);
            dispose();
        });
    }

    // Launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Home().setVisible(true);
        });
    }
}
