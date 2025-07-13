package com.ems.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import com.ems.db.DBConnection;

/**
 * DeboardEmployee.java
 * This window allows users to deboard (delete) an employee after verifying their details.
 * Functional steps:
 * - Enter employee ID
 * - Display employee details
 * - Confirm and delete the employee
 *
 * Author: Darshan Kumar GP
 */
public class DeboardEmployee extends JFrame {

    private JTextField idField;
    private JButton submitBtn, confirmBtn, backBtn;
    private JPanel detailPanel;
    private int currentId = -1;

    public DeboardEmployee() {
        // Frame setup
        setTitle("Employee Exit");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        // App icon
        setIconImage(new ImageIcon("images/ems_logo.png").getImage());

        // Title
        JLabel title = new JLabel("Employee Exit", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(200, 20, 400, 40);
        title.setOpaque(true);
        title.setBackground(Color.DARK_GRAY);
        title.setForeground(Color.WHITE);
        add(title);

        // Input for ID
        JLabel idLabel = new JLabel("Enter Employee ID:");
        idLabel.setBounds(200, 100, 150, 30);
        idField = new JTextField();
        idField.setBounds(350, 100, 150, 30);

        submitBtn = new JButton("Submit");
        submitBtn.setBounds(510, 100, 100, 30);
        submitBtn.setFocusPainted(false);
        submitBtn.addActionListener(e -> showEmployee());

        add(idLabel);
        add(idField);
        add(submitBtn);

        // Detail Panel (hidden until data is found)
        detailPanel = new JPanel();
        detailPanel.setLayout(new GridLayout(0, 1, 10, 10));
        detailPanel.setBounds(150, 160, 500, 250);
        detailPanel.setVisible(false);
        add(detailPanel);

        // Confirm Button
        confirmBtn = new JButton("Confirm Deboard");
        confirmBtn.setBounds(580, 430, 170, 35);
        confirmBtn.setBackground(new Color(204, 0, 0));
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setFocusPainted(false);
        confirmBtn.setVisible(false);
        confirmBtn.addActionListener(e -> confirmDeboard());
        add(confirmBtn);

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
     * Fetches employee details from DB and shows them if found.
     */
    private void showEmployee() {
        String idText = idField.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an employee ID.");
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
                currentId = id;
                detailPanel.add(new JLabel("ID: " + rs.getInt("id")));
                detailPanel.add(new JLabel("Name: " + rs.getString("name")));
                detailPanel.add(new JLabel("Salary: " + rs.getInt("salary")));
                detailPanel.add(new JLabel("Department: " + rs.getString("department")));
                detailPanel.add(new JLabel("Position: " + rs.getString("position")));

                detailPanel.setVisible(true);
                confirmBtn.setVisible(true);
                detailPanel.revalidate();
                detailPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Employee not found.");
                detailPanel.setVisible(false);
                confirmBtn.setVisible(false);
                currentId = -1;
            }

            conn.close();
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric ID.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "⚠️ Database error occurred.");
        }
    }

    /**
     * Deletes employee after confirmation.
     */
    private void confirmDeboard() {
        if (currentId == -1) return;

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to deboard this employee?",
            "Confirm",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = DBConnection.getConnection();
                String query = "DELETE FROM employees WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, currentId);
                int result = ps.executeUpdate();
                conn.close();

                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "✅ Employee successfully deboarded.");
                    detailPanel.setVisible(false);
                    confirmBtn.setVisible(false);
                    idField.setText("");
                    currentId = -1;
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Failed to deboard employee.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "⚠️ Database error.");
            }
        }
    }
}
