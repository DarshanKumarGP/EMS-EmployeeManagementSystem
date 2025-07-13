package com.ems.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import com.ems.db.DBConnection;

/**
 * DisplayAll.java
 * This class shows a table of all employees stored in the database.
 * The table is read-only with alternating row colors for better readability.
 * 
 * Author: Darshan Kumar GP
 * Project: EMS (Employee Management System)
 */
public class DisplayAll extends JFrame {

    private JTable table;

    public DisplayAll() {
        // Window setup
        setTitle("List of All Employees");
        setSize(800, 600);
        setLocationRelativeTo(null); // center window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        // Set app icon/logo
        setIconImage(new ImageIcon("images/ems_logo.png").getImage());

        // Title label
        JLabel title = new JLabel("LIST OF ALL EMPLOYEES", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(200, 20, 400, 40);
        title.setOpaque(true);
        title.setBackground(Color.DARK_GRAY);
        title.setForeground(Color.WHITE);
        add(title);

        // Table model and headers
        String[] columnNames = { "ID", "Name", "Salary", "Department", "Position" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setEnabled(false); // disable editing

        // Alternate row coloring for better UI
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                return c;
            }
        });

        // Load data from database
        loadData(model);

        // Table scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(100, 100, 600, 350);
        add(scrollPane);

        // Back Button
        JButton backBtn = new JButton("BACK");
        backBtn.setBounds(20, 510, 80, 30);
        backBtn.setBackground(Color.BLACK);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setToolTipText("Go back to Home");
        backBtn.addActionListener(e -> {
            new Home().setVisible(true);
            dispose();
        });
        add(backBtn);
    }

    /**
     * Fetch and load all employee records from the database into the table model.
     */
    private void loadData(DefaultTableModel model) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM employees")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int salary = rs.getInt("salary");
                String dept = rs.getString("department");
                String pos = rs.getString("position");

                model.addRow(new Object[]{id, name, salary, dept, pos});
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Failed to load employee data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
