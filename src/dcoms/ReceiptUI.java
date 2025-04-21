/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcoms;

/**
 *
 * @author User
 */
import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ReceiptUI extends JFrame {
    private Map<Integer, Integer> orderItems;
    private double totalAmount;
    private ItemService itemService;

    public ReceiptUI(Map<Integer, Integer> orderItems, double totalAmount, ItemService itemService) {
        this.orderItems = orderItems;
        this.totalAmount = totalAmount;
        this.itemService = itemService;
        
        initializeUI();
        setResizable(false);
    }

    private void initializeUI() {
        setTitle("Order Receipt");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JTextArea receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(receiptArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton printBtn = new JButton("Print receipt");
        JButton closeBtn = new JButton("Close");

        // Save/Print Button Action
        printBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Receipt As");
            fileChooser.setSelectedFile(new java.io.File("receipt.txt"));
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                try {
                    java.io.File fileToSave = fileChooser.getSelectedFile();
                    java.io.FileWriter writer = new java.io.FileWriter(fileToSave);
                    writer.write(receiptArea.getText());
                    writer.close();
                    JOptionPane.showMessageDialog(this, "Receipt saved successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
                }
            }
        });

        closeBtn.addActionListener(e -> dispose());

        buttonPanel.add(printBtn);
        buttonPanel.add(closeBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        buildReceipt(receiptArea);
    }


    private void buildReceipt(JTextArea area) {
        StringBuilder sb = new StringBuilder();
        sb.append("        FOOD ORDER RECEIPT\n");
        sb.append("  -------------------------------\n");
        sb.append("  Date: ").append(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())).append("\n");
        sb.append("  Order ID: #").append(System.currentTimeMillis() % 1000000).append("\n");
        sb.append("  -------------------------------\n\n");

        sb.append(String.format("%-25s %-10s %-10s\n", "Item", "Qty", "Price"));

        for (Map.Entry<Integer, Integer> entry : orderItems.entrySet()) {
            try {
                Item item = itemService.getItemById(entry.getKey());
                if (item != null) {
                    int qty = entry.getValue();
                    double price = item.getPrice() * qty;
                    sb.append(String.format("%-25s %-10d $%-9.2f\n", item.getItemName(), qty, price));
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        sb.append("\n  -------------------------------\n");
        sb.append(String.format("  Total: $%.2f\n", totalAmount));
        sb.append("  Thank you for your order!\n");

        area.setText(sb.toString());
    }
}

