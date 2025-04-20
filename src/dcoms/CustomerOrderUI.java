package dcoms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerOrderUI extends JFrame {
    private ItemService itemService;
    private List<Item> menuItems;
    private JPanel menuPanel;
    private JPanel cartPanel;
    private JLabel totalLabel;
    private double totalAmount = 0.0;
    private Map<Integer, Integer> orderItems = new HashMap<>(); 
    
    private String username;
    private String userType;
    
    public CustomerOrderUI(String username, String userType) {
        this.username = username;
        this.userType = userType;
        
        setupRMIConnection();
        initializeUI();
        loadMenuItems();
    }

    private CustomerOrderUI() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void setupRMIConnection() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            itemService = (ItemService) registry.lookup("ItemService");
        } catch (RemoteException | NotBoundException e) {
            JOptionPane.showMessageDialog(this, "Error connecting to server: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private void initializeUI() {
        setTitle("Food Ordering System");
        setSize(1100, 650);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Add header panel with Home button
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.setBackground(new Color(240, 240, 240));
        
        JButton homeButton = new JButton("Return to Home");
        homeButton.addActionListener(e -> goToHomePage());
        headerPanel.add(homeButton, BorderLayout.WEST);
        
        JLabel titleLabel = new JLabel("Food Ordering System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Menu Panel (Left side)
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane menuScrollPane = new JScrollPane(menuPanel);
        menuScrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Menu Items"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Cart Panel (Right side)
        cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
        cartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane cartScrollPane = new JScrollPane(cartPanel);
        cartScrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Your Order"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Bottom panel with total and checkout button
        JPanel bottomPanel = new JPanel(new BorderLayout(20, 0));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        bottomPanel.setBackground(new Color(245, 245, 245));
        
        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        JButton checkoutBtn = new JButton("Checkout");
        checkoutBtn.setFont(new Font("Arial", Font.BOLD, 14));
        checkoutBtn.setPreferredSize(new Dimension(120, 40));
        checkoutBtn.addActionListener(e -> checkout());
        
        bottomPanel.add(totalLabel, BorderLayout.WEST);
        bottomPanel.add(checkoutBtn, BorderLayout.EAST);
        
        // Split pane for menu and cart
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT, menuScrollPane, cartScrollPane);
        splitPane.setDividerLocation(600);
        
        add(splitPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void goToHomePage() {
        // Add actual home page navigation logic here
        this.setVisible(false);
        new CustomerHome(username, userType).setVisible(true);
    }
    
    private void loadMenuItems() {
        try {
            menuItems = itemService.getAllItems();
            displayMenuItems();
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Error loading menu items: " + e.getMessage(),
                                         "Data Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void displayMenuItems() {
        menuPanel.removeAll();
        
        // Group items by category
        Map<String, List<Item>> itemsByCategory = new HashMap<>();
        
        for (Item item : menuItems) {
            String category = item.getCategory();
            if (!itemsByCategory.containsKey(category)) {
                itemsByCategory.put(category, new ArrayList<>());
            }
            itemsByCategory.get(category).add(item);
        }
        
        // Create category panels
        for (String category : itemsByCategory.keySet()) {
            JPanel categoryPanel = new JPanel();
            categoryPanel.setLayout(new BorderLayout(0, 10));
            categoryPanel.setBorder(BorderFactory.createEmptyBorder(15, 5, 25, 5));
            
            // Category header with margins
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
            
            JLabel categoryLabel = new JLabel(category);
            categoryLabel.setFont(new Font("Arial", Font.BOLD, 18));
            headerPanel.add(categoryLabel, BorderLayout.WEST);
            
            categoryPanel.add(headerPanel, BorderLayout.NORTH);
            
            // Items grid with spacing
            JPanel itemsGrid = new JPanel();
            itemsGrid.setLayout(new GridLayout(0, 3, 15, 15));
            
            for (Item item : itemsByCategory.get(category)) {
                JPanel itemPanel = createItemPanel(item);
                itemsGrid.add(itemPanel);
            }
            
            categoryPanel.add(itemsGrid, BorderLayout.CENTER);
            
            menuPanel.add(categoryPanel);
            // Add separation between categories
            menuPanel.add(Box.createVerticalStrut(10));
        }
        
        menuPanel.revalidate();
        menuPanel.repaint();
    }
    
    private JPanel createItemPanel(Item item) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        panel.setPreferredSize(new Dimension(180, 200));
        
        // Image panel with padding
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Image label
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // Load image if available
        if (item.getImage() != null && !item.getImage().isEmpty()) {
            try {
                // Use resource loading instead of direct file loading
                ImageIcon icon = loadImageFromResource(item.getImage());
                if (icon != null) {
                    // Resize image to fit panel
                    Image image = icon.getImage().getScaledInstance(100, 80, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(image);
                    imageLabel.setIcon(scaledIcon);
                } else {
                    imageLabel.setText("No Image");
                }
            } catch (Exception e) {
                e.printStackTrace();
                imageLabel.setText("Error loading image");
            }
        } else {
            imageLabel.setText("No Image");
        }
        
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        
        // Item information
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        JLabel nameLabel = new JLabel(item.getItemName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel priceLabel = new JLabel("$" + String.format("%.2f", item.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(priceLabel);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Add to Cart");
        addButton.addActionListener(e -> addToCart(item));
        buttonPanel.add(addButton);
        
        panel.add(imagePanel, BorderLayout.NORTH);
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private ImageIcon loadImageFromResource(String imageName) {
//        String[] possiblePaths = {
//            "/images/" + imageName,
//            imageName
//        };
//        
//        // First try to load using resource
//        for (String path : possiblePaths) {
//            URL imageUrl = getClass().getResource(path);
//            if (imageUrl != null) {
//                return new ImageIcon(imageUrl);
//            }
//        }
              
        // try loading from the project's root directory
        String projectRoot = System.getProperty("user.dir");
        File file = new File(projectRoot + "/src/images/" + imageName);
        if (file.exists() && file.isFile()) {
            return new ImageIcon(file.getAbsolutePath());
        }
        
        System.out.println("Failed to load image: " + imageName);
        return null;
    }
    
    private void addToCart(Item item) {
        // Update order quantity
        int itemId = item.getItemId();
        int currentQty = orderItems.getOrDefault(itemId, 0);
        orderItems.put(itemId, currentQty + 1);
        
        totalAmount += item.getPrice();
        totalLabel.setText("Total: $" + String.format("%.2f", totalAmount));
        
        updateCartDisplay();
    }
    
    private void updateCartDisplay() {
    cartPanel.removeAll();
    
    for (Map.Entry<Integer, Integer> entry : orderItems.entrySet()) {
        int itemId = entry.getKey();
        int quantity = entry.getValue();
        
        try {
            Item item = itemService.getItemById(itemId);
            if (item != null) {
                // Create a panel with GridBagLayout for precise control
                JPanel itemPanel = new JPanel(new GridBagLayout());
                itemPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(10, 5, 10, 5)
                ));
                
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.VERTICAL;
                gbc.insets = new Insets(0, 5, 0, 5);
                
                // 1. Image on the left
                JLabel thumbLabel = new JLabel();
                if (item.getImage() != null && !item.getImage().isEmpty()) {
                    try {
                        ImageIcon icon = loadImageFromResource(item.getImage());
                        if (icon != null) {
                            Image image = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                            ImageIcon scaledIcon = new ImageIcon(image);
                            thumbLabel.setIcon(scaledIcon);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.weightx = 0.1;
                itemPanel.add(thumbLabel, gbc);
                
                // 2. Item name in the middle
                JLabel nameLabel = new JLabel(item.getItemName());
                nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
                gbc.gridx = 1;
                gbc.weightx = 0.4; // Give more space to name
                gbc.anchor = GridBagConstraints.WEST;
                itemPanel.add(nameLabel, gbc);
                
                // 3. Price calculation
                double itemTotal = item.getPrice() * quantity;
                JLabel priceLabel = new JLabel(quantity + "x $" + String.format("%.2f", item.getPrice()) + 
                        " = $" + String.format("%.2f", itemTotal));
                priceLabel.setForeground(new Color(0, 100, 0));
                gbc.gridx = 2;
                gbc.weightx = 0.3;
                itemPanel.add(priceLabel, gbc);
                
   
                JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
                
                JButton removeBtn = new JButton("-");
                removeBtn.setPreferredSize(new Dimension(40, 25));
                removeBtn.addActionListener(e -> removeFromCart(item));
                
                JLabel qtyLabel = new JLabel(String.valueOf(quantity), SwingConstants.CENTER);
                qtyLabel.setPreferredSize(new Dimension(20, 20));
                
                JButton addBtn = new JButton("+");
                addBtn.setPreferredSize(new Dimension(40, 25));
                addBtn.addActionListener(e -> addToCart(item));
                
                quantityPanel.add(removeBtn);
                quantityPanel.add(qtyLabel);
                quantityPanel.add(addBtn);
                
                gbc.gridx = 3;
                gbc.weightx = 0.2;
                gbc.anchor = GridBagConstraints.EAST;
                itemPanel.add(quantityPanel, gbc);
                
                cartPanel.add(itemPanel);
                cartPanel.add(Box.createVerticalStrut(2)); // Add a small gap between items
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    
        // Add empty space at the bottom
        cartPanel.add(Box.createVerticalGlue());

        cartPanel.revalidate();
        cartPanel.repaint();
    }

    private void removeFromCart(Item item) {
        int itemId = item.getItemId();
        if (orderItems.containsKey(itemId)) {
            int currentQty = orderItems.get(itemId);
            if (currentQty > 1) {
                orderItems.put(itemId, currentQty - 1);
            } else {
                orderItems.remove(itemId);
            }
            
            totalAmount -= item.getPrice();
            totalLabel.setText("Total: $" + String.format("%.2f", totalAmount));
            
            updateCartDisplay();
        }
    }
    
    private void checkout() {
        if (orderItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty!", "Empty Cart", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
         int result = JOptionPane.showConfirmDialog(this, 
         "Order placed successfully!\nTotal: $" + String.format("%.2f", totalAmount) + "\n\nView Receipt?", 
         "Order Confirmation", 
         JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
         // Open receipt window
        new ReceiptUI(orderItems, totalAmount, itemService).setVisible(true);
    }
        orderItems.clear();
        totalAmount = 0.0;
        totalLabel.setText("Total: $0.00");
        updateCartDisplay();
    }
    
    public static void main(String[] args) {
        try {
            // Set system look and feel for better appearance
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            CustomerOrderUI ui = new CustomerOrderUI();
            ui.setVisible(true);
        });
    }
}



