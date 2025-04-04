package dcoms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentUI {
    // This would normally be passed from the order page
    private static ArrayList<OrderItem> orderItems = new ArrayList<>();
    
    // Sample class to represent order items
    public static class OrderItem {
        String name;
        double price;
        int quantity;
        
        public OrderItem(String name, double price, int quantity) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }
        
        public double getSubtotal() {
            return price * quantity;
        }
    }
    
    // For demonstration, populate with sample items
    static {
        orderItems.add(new OrderItem("Burger", 5.99, 2));
        orderItems.add(new OrderItem("Pizza", 8.49, 1));
        orderItems.add(new OrderItem("Soda", 1.99, 3));
    }

    public static void showPaymentPage() {
        SwingUtilities.invokeLater(PaymentUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Payment");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setSize(700, 580);

        // --- Header Panel ---
        JPanel headerPanel = createHeaderPanel(frame);
        
        // --- Content Panel 
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        
        // Order Summary Panel 
        JPanel orderSummaryPanel = createOrderSummaryPanel();
        
        // Payment Details Panel
        JPanel paymentDetailsPanel = createPaymentDetailsPanel();
        
        // Add panels to content panel
        contentPanel.add(orderSummaryPanel, BorderLayout.CENTER);
        contentPanel.add(paymentDetailsPanel, BorderLayout.SOUTH);
        
        // Add panels to the main frame
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(contentPanel, BorderLayout.CENTER);
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private static JPanel createHeaderPanel(JFrame frame) {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout(10, 0));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        headerPanel.setBackground(new Color(240, 240, 240));
        
        JButton backButton = new JButton("<< Back to Menu");
        JLabel titleLabel = new JLabel("Checkout", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        backButton.addActionListener(e -> {
            System.out.println("Back to Order Page button clicked!");
            frame.dispose();
            CustomerOrderUI.showOrderPage(); 
        });
        
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private static JPanel createOrderSummaryPanel() {
        JPanel orderSummaryPanel = new JPanel(new BorderLayout(0, 10));
        orderSummaryPanel.setBorder(BorderFactory.createTitledBorder("Order Summary"));
        
        // Create table model for order items
        String[] columnNames = {"Item", "Price", "Quantity", "Subtotal"};
        Object[][] data = new Object[orderItems.size()][4];
        
        double total = 0.0;
        for (int i = 0; i < orderItems.size(); i++) {
            OrderItem item = orderItems.get(i);
            data[i][0] = item.name;
            data[i][1] = String.format("$%.2f", item.price);
            data[i][2] = item.quantity;
            data[i][3] = String.format("$%.2f", item.getSubtotal());
            total += item.getSubtotal();
        }
        
        JTable orderTable = new JTable(data, columnNames);
        orderTable.setEnabled(false);
        orderTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setPreferredSize(new Dimension(500, 150));
        
        // Total panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel totalLabel = new JLabel("Total: $" + String.format("%.2f", total), SwingConstants.RIGHT);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPanel.add(totalLabel);
        
        orderSummaryPanel.add(scrollPane, BorderLayout.CENTER);
        orderSummaryPanel.add(totalPanel, BorderLayout.SOUTH);
        
        return orderSummaryPanel;
    }
    
    private static JPanel createPaymentDetailsPanel() {
        JPanel paymentDetailsPanel = new JPanel();
        paymentDetailsPanel.setLayout(new BoxLayout(paymentDetailsPanel, BoxLayout.Y_AXIS));
        paymentDetailsPanel.setBorder(BorderFactory.createTitledBorder("Payment Details"));
        
        // Payment method selection
        JPanel paymentMethodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel paymentMethodLabel = new JLabel("Payment Method:");
        String[] paymentMethods = {"Credit Card", "Debit Card", "Cash on Delivery"};
        JComboBox<String> paymentMethodComboBox = new JComboBox<>(paymentMethods);
        paymentMethodPanel.add(paymentMethodLabel);
        paymentMethodPanel.add(paymentMethodComboBox);
        

        
        // Payment method change listener
        paymentMethodComboBox.addActionListener(e -> {
            String selectedMethod = (String) paymentMethodComboBox.getSelectedItem();
            paymentDetailsPanel.revalidate();
            paymentDetailsPanel.repaint();
        });
        
        // Complete order button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton completeOrderButton = new JButton("Complete Order");
        completeOrderButton.setPreferredSize(new Dimension(150, 40));
        completeOrderButton.setBackground(new Color(46, 204, 113));
        completeOrderButton.setForeground(Color.WHITE);
        completeOrderButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        completeOrderButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                null,
                "Thank you for your order! Payment successful.",
                "Order Completed",
                JOptionPane.INFORMATION_MESSAGE
            );
            System.out.println("Order completed with payment method: " + paymentMethodComboBox.getSelectedItem());
            // Here you would normally process the payment and navigate to a confirmation page
        });
        
        buttonPanel.add(completeOrderButton);
    
        paymentDetailsPanel.add(paymentMethodPanel);
        paymentDetailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        paymentDetailsPanel.add(buttonPanel);
        
        return paymentDetailsPanel;
    }

    public static void main(String[] args) {
        showPaymentPage();
    }
}
