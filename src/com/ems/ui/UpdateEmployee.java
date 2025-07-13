package com.ems.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import com.ems.db.DBConnection;

/**
 * UpdateEmployee.java
 * Allows updating details of an employee by entering their ID and modifying fields.
 *
 * Author: Darshan Kumar GP
 */
public class UpdateEmployee extends JFrame {

    private JTextField idField, nameField, salaryField, deptField, positionField;
    private JButton submitBtn, updateBtn, backBtn;

    public UpdateEmployee() {
        setTitle("Update Employee Details");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        // App icon
        setIconImage(new ImageIcon("images/ems_logo.png").getImage());

        // Page title
        JLabel title = new JLabel("Update Employee Details", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(200, 20, 400, 40);
        title.setOpaque(true);
        title.setBackground(Color.DARK_GRAY);
        title.setForeground(Color.WHITE);
        add(title);

        // ID input section
        JLabel idLabel = new JLabel("Enter Employee ID:");
        idLabel.setBounds(200, 100, 150, 30);
        idField = new JTextField();
        idField.setBounds(350, 100, 150, 30);

        submitBtn = new JButton("Submit");
        submitBtn.setBounds(510, 100, 100, 30);
        submitBtn.setBackground(new Color(30, 144, 255));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);
        submitBtn.setToolTipText("Fetch employee data");
        submitBtn.addActionListener(e -> fetchEmployee());

        add(idLabel);
        add(idField);
        add(submitBtn);

        // Form fields
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(200, 160, 100, 30);
        nameField = new JTextField();
        nameField.setBounds(350, 160, 200, 30);

        JLabel salaryLabel = new JLabel("Salary:");
        salaryLabel.setBounds(200, 210, 100, 30);
        salaryField = new JTextField();
        salaryField.setBounds(350, 210, 200, 30);

        JLabel deptLabel = new JLabel("Department:");
        deptLabel.setBounds(200, 260, 100, 30);
        deptField = new JTextField();
        deptField.setBounds(350, 260, 200, 30);

        JLabel positionLabel = new JLabel("Position:");
        positionLabel.setBounds(200, 310, 100, 30);
        positionField = new JTextField();
        positionField.setBounds(350, 310, 200, 30);

        // Update button
        updateBtn = new JButton("Update");
        updateBtn.setBounds(350, 370, 120, 35);
        updateBtn.setBackground(new Color(30, 144, 255));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFocusPainted(false);
        updateBtn.addActionListener(e -> updateEmployee());

        // Back button
        backBtn = new JButton("BACK");
        backBtn.setBounds(20, 510, 80, 30);
        backBtn.setBackground(Color.BLACK);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> {
            new Home().setVisible(true);
            dispose();
        });

        // Add components
        add(nameLabel);
        add(nameField);
        add(salaryLabel);
        add(salaryField);
        add(deptLabel);
        add(deptField);
        add(positionLabel);
        add(positionField);
        add(updateBtn);
        add(backBtn);

        // Hide initially
        setFieldsVisible(false);
        setLabelsVisible(false);
    }

    /**
     * Fetches employee details from DB and shows fields for editing.
     */
    private void fetchEmployee() {
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

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                salaryField.setText(String.valueOf(rs.getInt("salary")));
                deptField.setText(rs.getString("department"));
                positionField.setText(rs.getString("position"));

                setFieldsVisible(true);
                setLabelsVisible(true);
                updateBtn.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Employee not found.");
            }

            conn.close();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ ID must be numeric.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Database error.");
        }
    }

    /**
     * Updates employee details in DB using entered values.
     */
    private void updateEmployee() {
        int confirm = JOptionPane.showConfirmDialog(this, "Confirm update?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String name = nameField.getText().trim();
                int salary = Integer.parseInt(salaryField.getText().trim());
                String dept = deptField.getText().trim();
                String pos = positionField.getText().trim();

                Connection conn = DBConnection.getConnection();
                String query = "UPDATE employees SET name=?, salary=?, department=?, position=? WHERE id=?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, name);
                ps.setInt(2, salary);
                ps.setString(3, dept);
                ps.setString(4, pos);
                ps.setInt(5, id);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "✅ Employee updated successfully.");
                    clearFields();
                    setFieldsVisible(false);
                    setLabelsVisible(false);
                    updateBtn.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Update failed.");
                }

                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "❌ Error during update.");
            }
        }
    }

    /** Utility: Set form fields visible/invisible */
    private void setFieldsVisible(boolean visible) {
        nameField.setVisible(visible);
        salaryField.setVisible(visible);
        deptField.setVisible(visible);
        positionField.setVisible(visible);
    }

    /** Utility: Set form labels visible/invisible */
    private void setLabelsVisible(boolean visible) {
        for (Component comp : getContentPane().getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getText().equals("Name:") ||
                    label.getText().equals("Salary:") ||
                    label.getText().equals("Department:") ||
                    label.getText().equals("Position:")) {
                    label.setVisible(visible);
                }
            }
        }
    }

    /** Utility: Clear all editable fields */
    private void clearFields() {
        nameField.setText("");
        salaryField.setText("");
        deptField.setText("");
        positionField.setText("");
    }
}
