package dcoms;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import com.toedter.calendar.JDateChooser;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SalesReport2 extends JFrame {
    private DefaultTableModel model;
    private JTable table;
    private JDateChooser dateFromChooser;
    private JDateChooser dateToChooser;
    private JTextField summaryField;
    java.util.List<Sale> salesList = new ArrayList<>();
    Date fromDate = null, toDate = null;

    public SalesReport2() {
        initializeUI();
        getReportDetails();
        displayReport();
    }

    private void initializeUI(){
        setTitle("Sales Report");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === Top Panel (Back Button + Date Filter) ===
        JPanel topPanel = new JPanel(new BorderLayout());

        // Back button (left side)
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backBtn = new JButton("Back to Home");
        backPanel.add(backBtn);

        // Filter components (right side)
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        dateFromChooser = new JDateChooser();
        dateToChooser = new JDateChooser();
        JButton filterBtn = new JButton("Filter");
        JButton clearBtn = new JButton("Clear");

        filterPanel.add(new JLabel("Date From:"));
        filterPanel.add(dateFromChooser);
        filterPanel.add(new JLabel("To:"));
        filterPanel.add(dateToChooser);
        filterPanel.add(filterBtn);
        filterPanel.add(clearBtn);

        topPanel.add(backPanel, BorderLayout.WEST);
        topPanel.add(filterPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // === Table Setup ===
        String[] columns = {
            "ID", "Item Name", "Unit Price (RM)", "Quantity Sold", "Total Amount (RM)"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // === Bottom Panel for Summary ===
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel summaryLabel = new JLabel("Total Sales (RM): ");
        summaryField = new JTextField(10);
        summaryField.setEditable(false);
        bottomPanel.add(summaryLabel);
        bottomPanel.add(summaryField);
        add(bottomPanel, BorderLayout.SOUTH);

        // === Button Actions ===
        filterBtn.addActionListener(e -> {
            fromDate = dateFromChooser.getDate();
            toDate = dateToChooser.getDate();
            Date today = new Date();

            if (fromDate != null && toDate != null) {
                if (fromDate.after(toDate)) {
                    JOptionPane.showMessageDialog(this, "'From' date cannot be after 'To' date.", "Invalid Date Range", JOptionPane.ERROR_MESSAGE);
                } else if (toDate.after(today)) {
                    JOptionPane.showMessageDialog(this, "'To' date cannot be in the future.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Implement filtering logic here
                    getReportDetails();
                    displayReport();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select both dates!", "Missing Dates", JOptionPane.WARNING_MESSAGE);
            }
        });

        clearBtn.addActionListener(e -> {
            dateFromChooser.setDate(null);
            dateToChooser.setDate(null);
            fromDate = null;
            toDate = null;
            getReportDetails();
            displayReport();
        });

        backBtn.addActionListener(e -> {
            dispose();
            new AdminHome().setVisible(true);
        });

        setVisible(true);
    }

    private void getReportDetails() {
        try {
            model.setRowCount(0);
            ReportService obj1 = (ReportService) Naming.lookup("rmi://localhost:1099/Report");
            if (fromDate == null && toDate == null){
                obj1.unfilteredQuery();
            } else {
                obj1.setFilterDate(fromDate, toDate);
                obj1.filteredQuery();
            }
            salesList = obj1.getReportDetails();
        } catch (RemoteException ex) {
            Logger.getLogger(SalesReport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(SalesReport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(SalesReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void displayReport() {
        double totalSales = 0.00;
        for (Sale sale : salesList) {
            totalSales = totalSales + sale.totalAmount;
            model.insertRow(model.getRowCount(), new Object[]{sale.itemID, sale.itemName, String.format("%.2f", sale.price), sale.quantity, String.format("%.2f", sale.totalAmount)});
        }
        summaryField.setText(String.format("%.2f", totalSales));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SalesReport2::new);
    }
}
