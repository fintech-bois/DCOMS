package dcoms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerOrderUI {

    public static void showOrderPage() {
        SwingUtilities.invokeLater(CustomerOrderUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Food & Beverage Ordering");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setSize(700, 580); 

        // --- Header Panel ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout(10, 0)); // for edge/center placement
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); 

        JButton backButton = new JButton("<< Back to Main Page");
        JLabel menu = new JLabel("Menu"); 
        JButton paymentButton = new JButton("Proceed to Payment >>");

        // Action Listeners (implement actual logic later)
        backButton.addActionListener(e -> {
            System.out.println("Back to Main Page button clicked!");
            // MainPageUI.showMainPage(); // Example call
        });

        paymentButton.addActionListener(e -> {
            System.out.println("Proceed to Payment button clicked!");    
            frame.dispose();
            PaymentUI.showPaymentPage();
        });

        // Panel to center the Menu button
        JPanel centerHeaderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerHeaderPanel.add(menu);

        headerPanel.add(backButton, BorderLayout.WEST);    
        headerPanel.add(centerHeaderPanel, BorderLayout.CENTER); 
        headerPanel.add(paymentButton, BorderLayout.EAST);   

        //Item Grid Panel
        JPanel itemGridPanel = new JPanel();
        itemGridPanel.setLayout(new GridLayout(2, 2, 25, 25)); // Increased gaps slightly
        itemGridPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15)); // Added padding

        String[] itemNames = {"Burger", "Pizza", "Soda", "Coffee"};
        String[] imagePaths = {"src/images/burger.jpg", "src/images/pizza.jpg", "src/images/soda.jpg", "src/images/coffee.jpg"};
        double[] prices = {5.99, 8.49, 1.99, 2.99};

        for (int i = 0; i < itemNames.length; i++) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BorderLayout(5, 5));
            itemPanel.setBorder(BorderFactory.createEtchedBorder());

            
            ImageIcon originalIcon = new ImageIcon(imagePaths[i]);
             if (originalIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                 System.err.println("Warning: Could not load image " + imagePaths[i]);
                 originalIcon = new ImageIcon();
            }
            Image img = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(img);
            JLabel imageLabel = new JLabel(scaledIcon);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel nameLabel = new JLabel(itemNames[i] + " - $" + String.format("%.2f", prices[i]), SwingConstants.CENTER);

            // Bottom Controls Panel
            JPanel bottomControlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

            bottomControlsPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0)); 

            JLabel quantityLabel = new JLabel("Qty:");
            JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
            quantitySpinner.setPreferredSize(new Dimension(50, (int) quantitySpinner.getPreferredSize().getHeight()));
            JButton addToCart = new JButton("Add to Cart");

             addToCart.addActionListener(e -> {
                 int quantity = (Integer) quantitySpinner.getValue();
                 if (quantity > 0) {
                     String itemName = nameLabel.getText().split(" - ")[0];
                     System.out.println("Adding " + quantity + " of " + itemName + " to cart.");
                     // Add actual cart logic here
                 } else {
                     System.out.println("Select a quantity greater than 0.");
                     // JOptionPane.showMessageDialog(frame, "Please select a quantity greater than 0.", "Info", JOptionPane.INFORMATION_MESSAGE);
                 }
             });

            bottomControlsPanel.add(quantityLabel);
            bottomControlsPanel.add(quantitySpinner);
            bottomControlsPanel.add(addToCart);

            
            itemPanel.add(nameLabel, BorderLayout.NORTH);
            itemPanel.add(imageLabel, BorderLayout.CENTER);
            itemPanel.add(bottomControlsPanel, BorderLayout.SOUTH);

            // Add the complete item panel to the item GRID panel
            itemGridPanel.add(itemPanel);
        }

        // --- Add Header and Item Grid to the Main Frame 
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(itemGridPanel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        showOrderPage();
    }
}