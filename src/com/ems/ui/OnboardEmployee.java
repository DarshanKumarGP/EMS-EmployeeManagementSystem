package com.ems.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.ems.db.DBConnection;

/**
 * OnboardEmployee.java
 * Window for onboarding new employees into the EMS.
 * Users can input name, salary, department, and position.
 * On successful submission, employee data is inserted into the database.
 *
 * Author: Darshan Kumar GP
 */
public class OnboardEmployee extends JFrame {

    private JTextField nameField, salaryField, deptField, positionField;
    private JButton onboardBtn;

    public OnboardEmployee() {
        setTitle("Employee Onboarding");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        // Set logo icon
        setIconImage(new ImageIcon("images/ems_logo.png").getImage());

        // Title
        JLabel title = new JLabel("Employee Onboarding", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(200, 20, 400, 40);
        title.setOpaque(true);
        title.setBackground(Color.DARK_GRAY);
        title.setForeground(Color.WHITE);
        add(title);

        // Labels & Fields
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(200, 100, 100, 30);
        nameField = new JTextField();
        nameField.setBounds(350, 100, 200, 30);

        JLabel salaryLabel = new JLabel("Salary:");
        salaryLabel.setBounds(200, 150, 100, 30);
        salaryField = new JTextField();
        salaryField.setBounds(350, 150, 200, 30);

        JLabel deptLabel = new JLabel("Department:");
        deptLabel.setBounds(200, 200, 100, 30);
        deptField = new JTextField();
        deptField.setBounds(350, 200, 200, 30);

        JLabel positionLabel = new JLabel("Position:");
        positionLabel.setBounds(200, 250, 100, 30);
        positionField = new JTextField();
        positionField.setBounds(350, 250, 200, 30);

        // Onboard Button
        onboardBtn = new JButton("Onboard");
        onboardBtn.setBounds(350, 320, 120, 35);
        onboardBtn.setBackground(new Color(0, 153, 76)); // Green
        onboardBtn.setForeground(Color.WHITE);
        onboardBtn.setFocusPainted(false);
        onboardBtn.addActionListener(this::onboardEmployee);

        // Back Button
        JButton backBtn = new JButton("BACK");
        backBtn.setBounds(20, 510, 80, 30);
        backBtn.setBackground(Color.BLACK);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setToolTipText("Return to Home");
        backBtn.addActionListener(e -> {
            new Home().setVisible(true);
            dispose();
        });

        // Add components
        add(nameLabel);     add(nameField);
        add(salaryLabel);   add(salaryField);
        add(deptLabel);     add(deptField);
        add(positionLabel); add(positionField);
        add(onboardBtn);    add(backBtn);
    }

    /**
     * Insert employee into the database after validation.
     */
    private void onboardEmployee(ActionEvent e) {
        String name = nameField.getText().trim();
        String salaryText = salaryField.getText().trim();
        String dept = deptField.getText().trim();
        String position = positionField.getText().trim();

        if (name.isEmpty() || salaryText.isEmpty() || dept.isEmpty() || position.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ All fields are required.");
            return;
        }

        try {
            int salary = Integer.parseInt(salaryText);
            Connection conn = DBConnection.getConnection();
            String query = "INSERT INTO employees (name, salary, department, position) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, name);
            ps.setInt(2, salary);
            ps.setString(3, dept);
            ps.setString(4, position);

            int rows = ps.executeUpdate();
            conn.close();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "✅ Onboarding successful.");
                nameField.setText("");
                salaryField.setText("");
                deptField.setText("");
                positionField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Onboarding failed.");
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "⚠️ Salary must be a numeric value.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Invalid input or database error.");
        }
    }
}
