package dcoms;

import javax.swing.*;
import java.awt.*;

public class AdminHomePage extends JFrame {
    private String username;
    private String userType;

    public AdminHomePage(String username, String userType) {
        this.username = username;
        this.userType = userType;

        // Set window properties
        setTitle("Admin Homepage");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setResizable(false);

        // Load and scale background image
        ImageIcon imageIcon;
        try {
            imageIcon = new ImageIcon(getClass().getClassLoader().getResource("images/AdminHome.jpeg"));
            if (imageIcon.getIconWidth() == -1) {
                throw new Exception("Image not found");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to load background image!", "Error", JOptionPane.ERROR_MESSAGE);
            imageIcon = new ImageIcon(); // fallback
        }

        Image scaledImage = imageIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        JLabel backgroundLabel = new JLabel(new ImageIcon(scaledImage));
        backgroundLabel.setLayout(new BorderLayout());

        // Top panel for Logout button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        topPanel.setOpaque(false);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogout.setBackground(Color.RED);
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnLogout.addActionListener(e -> logoutActionPerformed());

        topPanel.add(btnLogout);
        backgroundLabel.add(topPanel, BorderLayout.NORTH);

        // Center panel for feature buttons
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 15, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(250, 250));

        JButton btnInventory = createStyledButton("CRUD Inventory");
        JButton btnSales = createStyledButton("Sales Report");
        JButton btnEdit = createStyledButton("Edit Profile");
        JButton btnCreateAdmin = createStyledButton("Create New Admin");

        btnInventory.addActionListener(e -> inventoryActionPerformed());
        btnSales.addActionListener(e -> salesReportActionPerformed());
        btnEdit.addActionListener(e -> editProfileActionPerformed());
        btnCreateAdmin.addActionListener(e -> createAdminActionPerformed());

        buttonPanel.add(btnInventory);
        buttonPanel.add(btnSales);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnCreateAdmin);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(buttonPanel);

        backgroundLabel.add(centerWrapper, BorderLayout.CENTER);
        setContentPane(backgroundLabel);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    // Button actions
    private void salesReportActionPerformed() {
        SalesReport2 a = new SalesReport2(username, userType);
        a.setVisible(true);
        this.setVisible(false);
    }

    private void editProfileActionPerformed() {
        ProfileEdit edit = new ProfileEdit(username, userType);
        edit.setVisible(true);
        this.setVisible(false);
    }

    private void createAdminActionPerformed() {
        AdminSignup signup = new AdminSignup(username, userType);
        signup.setVisible(true);
        this.setVisible(false);
    }

    private void logoutActionPerformed() {
        Login a = new Login();
        a.setVisible(true);
        this.setVisible(false);
    }

    private void inventoryActionPerformed() {
        AdminUI adminUI = new AdminUI(username, userType);
        adminUI.setVisible(true);
        this.setVisible(false);
    }

    public static void main(String[] args) {
        // Sample test
        SwingUtilities.invokeLater(() -> new AdminHomePage("adminUser", "Admin"));
    }
}
