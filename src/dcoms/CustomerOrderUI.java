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
        frame.setSize(600, 400);
        frame.setLayout(new GridLayout(2, 2)); // Adjust grid size based on number of items

        String[] itemNames = {"Burger", "Pizza", "Soda", "Coffee"};
        String[] imagePaths = {"src/images/burger.jpg", "src/images/pizza.jpg", "src/images/burger.jpg", "src/images/pizza.jpg"};
        double[] prices = {5.99, 8.49, 1.99, 2.99};

        for (int i = 0; i < itemNames.length; i++) {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());

            // Load Image
            ImageIcon originalIcon = new ImageIcon(imagePaths[i]);
            Image img = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(img);
            
            JLabel imageLabel = new JLabel(scaledIcon);
            JLabel nameLabel = new JLabel(itemNames[i] + " - $" + prices[i], SwingConstants.CENTER);
            JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
            JButton addToCart = new JButton("Add to Cart");

            panel.add(imageLabel, BorderLayout.CENTER);
            panel.add(nameLabel, BorderLayout.NORTH);
            panel.add(quantitySpinner, BorderLayout.WEST);
            panel.add(addToCart, BorderLayout.SOUTH);

            frame.add(panel);
        }

        frame.setVisible(true);
    }
}
