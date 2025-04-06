package dcoms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaymentUI {
    private JFrame frame;
    private JLabel totalLabel;
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private PaymentOperations operations;
    
    public PaymentUI() {
        operations = new PaymentOperations();
    }
    
    public PaymentUI(PaymentOperations operations) {
        this.operations = operations;
    }

    public void showPaymentPage() {
        SwingUtilities.invokeLater(this::createAndShowGUI);
    }

    private void createAndShowGUI() {
        frame = new JFrame("Payment");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setSize(800, 600);
        

        // --- Header Panel ---
        JPanel headerPanel = createHeaderPanel();
        
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
    
    private JPanel createHeaderPanel() {
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
    
    private JPanel createOrderSummaryPanel() {
        JPanel orderSummaryPanel = new JPanel(new BorderLayout(0, 10));
        orderSummaryPanel.setBorder(BorderFactory.createTitledBorder("Order Summary"));
        
        String[] columnNames = {"Item", "Price", "Quantity", "Subtotal", "Action"};
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        for (String columnName : columnNames) {
            tableModel.addColumn(columnName);
        }
        updateTableData();
        
        // Create table with the model
        orderTable = new JTable(tableModel);
        orderTable.getTableHeader().setReorderingAllowed(false);
        
        // render: how to display each cell and handle when clicked
        orderTable.getColumn("Action").setCellRenderer(new ButtonRenderer("Remove Item"));
        orderTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), "Remove Item "));
        
        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setPreferredSize(new Dimension(500, 150));
        
        // Total panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalLabel = new JLabel("Total: $" + String.format("%.2f", operations.getTotalAmount()), SwingConstants.RIGHT);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPanel.add(totalLabel);
        
        // Add an empty cart message if needed
        if (operations.isCartEmpty()) {
            JLabel emptyCartLabel = new JLabel("Your cart is empty. Please add items from the menu.", SwingConstants.CENTER);
            emptyCartLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            emptyCartLabel.setForeground(Color.GRAY);
            orderSummaryPanel.add(emptyCartLabel, BorderLayout.NORTH);
        }
        
        orderSummaryPanel.add(scrollPane, BorderLayout.CENTER);
        orderSummaryPanel.add(totalPanel, BorderLayout.SOUTH);
        
        return orderSummaryPanel;
    }
    
    // Update the table data and recalculate the total
    private void updateTableData() {
        // Clear existing rows
        tableModel.setRowCount(0);
        
        // Add items to table
        for (int i = 0; i < operations.getOrderItems().size(); i++) {
            PaymentOperations.OrderItem item = operations.getOrderItems().get(i);
            double subtotal = item.getSubtotal();
            
            tableModel.addRow(new Object[]{
                item.name,
                String.format("$%.2f", item.price),
                item.quantity,
                String.format("$%.2f", subtotal),
                "Remove item"
            });
        }
        
        // Update the total label if it exists
        if (totalLabel != null) {
            totalLabel.setText("Total: $" + String.format("%.2f", operations.getTotalAmount()));
        }
        
        // Check if cart is empty and display message
        if (operations.getOrderItems().isEmpty()) {
            System.out.println("The cart is empty");
        }
    }
    
    private JPanel createPaymentDetailsPanel() {
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
              
        // Complete order button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton completeOrderButton = new JButton("Complete Order");
        completeOrderButton.setPreferredSize(new Dimension(150, 40));
        completeOrderButton.setBackground(new Color(46, 204, 113));
        completeOrderButton.setForeground(Color.WHITE);
        completeOrderButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        completeOrderButton.addActionListener(e -> {
            if (operations.isCartEmpty()) {
                JOptionPane.showMessageDialog(
                    null,
                    "Your cart is empty. Please add items before completing your order.",
                    "Empty Cart",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            
            double orderTotal = operations.getTotalAmount();
            String selectedPaymentMethod = (String) paymentMethodComboBox.getSelectedItem();
            boolean orderCompleted = operations.completeOrder(selectedPaymentMethod);
            
            if (orderCompleted) {
                JOptionPane.showMessageDialog(
                    null,
                    "Thank you for your order! Payment successful.\nTotal Amount: $" + 
                    String.format("%.2f", orderTotal),
                    "Order Completed",
                    JOptionPane.INFORMATION_MESSAGE
                );

                frame.dispose();
                CustomerOrderUI.showOrderPage();
            } else {
                JOptionPane.showMessageDialog(
                    null,
                    "There was a problem processing your payment. Please try again.",
                    "Payment Failed",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
        
        buttonPanel.add(completeOrderButton);
        paymentDetailsPanel.add(paymentMethodPanel);
        paymentDetailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        paymentDetailsPanel.add(buttonPanel);
        
        return paymentDetailsPanel;
    }
    
    // Custom button renderer for the table
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer(String text) {
            setOpaque(true);
            setForeground(Color.RED);
            setText(text);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private int rowToModify;  

        public ButtonEditor(JCheckBox checkBox, String text) {
            super(checkBox);
            button = new JButton(text);
            button.setOpaque(true);
            button.setForeground(Color.RED);             
            // Use SwingUtilities.invokeLater to handle the action after the cell editing is complete
            button.addActionListener(e -> {
                isPushed = true;
                fireEditingStopped();
            });
        }
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            rowToModify = row;  
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        operations.decrementItemQuantity(rowToModify);
                        updateTableData();
                    } catch (Exception ex) {
                        System.out.println("Error when modifying item: " + ex.getMessage());
                    }
                });
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    public static void main(String[] args) {
        new PaymentUI().showPaymentPage();
    }
}