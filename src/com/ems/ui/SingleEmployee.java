package com.ems.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import com.ems.db.DBConnection;

/**
 * SingleEmployee.java
 * Window to view a specific employee's details by ID.
 * On valid ID input, displays employee information in a panel.
 *
 * Author: Darshan Kumar GP
 */
public class SingleEmployee extends JFrame {

    private JTextField idField;
    private JButton submitBtn, backBtn;
    private JPanel detailPanel;

    public SingleEmployee() {
        setTitle("Employee Details");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        // Set application icon
        setIconImage(new ImageIcon("images/ems_logo.png").getImage());

        // Page title
        JLabel title = new JLabel("Employee Details", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(200, 20, 400, 40);
        title.setOpaque(true);
        title.setBackground(Color.DARK_GRAY);
        title.setForeground(Color.WHITE);
        add(title);

        // Input Section
        JLabel idLabel = new JLabel("Enter Employee ID:");
        idLabel.setBounds(200, 100, 150, 30);
        idField = new JTextField();
        idField.setBounds(350, 100, 150, 30);
        idField.setToolTipText("Enter a valid numeric Employee ID");

        submitBtn = new JButton("Submit");
        submitBtn.setBounds(510, 100, 100, 30);
        submitBtn.setBackground(new Color(30, 144, 255));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);
        submitBtn.setToolTipText("Fetch employee details");
        submitBtn.addActionListener(e -> showEmployee());

        add(idLabel);
        add(idField);
        add(submitBtn);

        // Details Panel
        detailPanel = new JPanel();
        detailPanel.setLayout(new GridLayout(0, 1, 10, 10));
        detailPanel.setBounds(150, 160, 500, 300);
        detailPanel.setVisible(false);
        add(detailPanel);

        // Back Button
        backBtn = new JButton("BACK");
        backBtn.setBounds(20, 510, 80, 30);
        backBtn.setBackground(Color.BLACK);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setToolTipText("Return to Home");
        backBtn.addActionListener(e -> {
            new Home().setVisible(true);
            dispose();
        });

        add(backBtn);
    }

    /**
     * Fetches and displays the employee details from database using ID input.
     */
    private void showEmployee() {
        String idText = idField.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Please enter an employee ID.");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            Connection conn = DBConnection.getConnection();
            String query = "SELECT * FROM employees WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            detailPanel.removeAll();

            if (rs.next()) {
                detailPanel.add(new JLabel("ID: " + rs.getInt("id")));
                detailPanel.add(new JLabel("Name: " + rs.getString("name")));
                detailPanel.add(new JLabel("Salary: ₹" + rs.getInt("salary")));
                detailPanel.add(new JLabel("Department: " + rs.getString("department")));
                detailPanel.add(new JLabel("Position: " + rs.getString("position")));

                detailPanel.setVisible(true);
                detailPanel.revalidate();
                detailPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Employee not found.");
                detailPanel.setVisible(false);
            }

            conn.close();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ ID must be a number.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Database error or invalid input.");
        }
    }
}
