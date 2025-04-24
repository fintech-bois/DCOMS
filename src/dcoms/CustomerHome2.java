package dcoms;

import java.awt.*;
import javax.swing.*;

public class CustomerHome2 extends javax.swing.JFrame {

    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton logout;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;

    private String username;
    private String userType;

    public CustomerHome2(String username, String userType) {
        this.username = username;
        this.userType = userType;
        initComponents();
    }

    private void initComponents() {

        // === Top Navigation Panel ===
        setResizable(false);
        JPanel topNav = new JPanel(new BorderLayout());
        topNav.setBackground(new Color(0, 0, 0, 180));
        topNav.setPreferredSize(new Dimension(600, 50));
        topNav.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Title label
        JLabel navTitle = new JLabel("Customer Dashboard");         
        navTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        navTitle.setForeground(Color.WHITE);
        topNav.add(navTitle, BorderLayout.WEST);

        // Buttons panel
        JPanel navButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        navButtons.setOpaque(false);

        jButton2 = new JButton("Edit Profile");
        logout = new JButton("Logout");

        // === Button Styles ===
        jButton2.setBackground(new Color(30, 136, 229));     
        logout.setBackground(new Color(244, 67, 54));       

        for (JButton btn : new JButton[]{jButton2, logout}) {
            btn.setFocusPainted(false);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            btn.setContentAreaFilled(false);   
            btn.setOpaque(true);              
            btn.setBorderPainted(false);       
        }

        navButtons.add(jButton2);
        navButtons.add(logout);
        topNav.add(navButtons, BorderLayout.EAST);

        // === Labels and Button ===
        jLabel2 = new JLabel("Welcome back, " + username + "!");
        jLabel2.setFont(new Font("Segoe UI", Font.BOLD, 15));
        jLabel2.setForeground(Color.BLACK);

        jLabel3 = new JLabel("We are dedicated to bringing you the finest food and beverage.");
        jLabel3.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jLabel3.setForeground(Color.BLACK);

        jLabel5 = new JLabel("Enjoy convenience, variety, and top-notch service with every order!");
        jLabel5.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jLabel5.setForeground(Color.BLACK);

        jLabel4 = new JLabel("Get ready to order your food & beverage!");
        jLabel4.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        jLabel4.setForeground(Color.BLACK);

        jButton1 = new JButton("Order now");
        jButton1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jButton1.setBackground(new Color(76, 175, 80));
        jButton1.setForeground(Color.WHITE);
        jButton1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // === Load and Setup Background Image ===
        ImageIcon imageIcon;
        try {
            imageIcon = new ImageIcon(getClass().getClassLoader().getResource("images/home.jpg"));
            if (imageIcon.getIconWidth() == -1) {
                throw new Exception("Image not found");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to load background image!", "Error", JOptionPane.ERROR_MESSAGE);
            imageIcon = new ImageIcon(); // fallback to empty icon
        }

        // Scale image
        Image scaledImage = imageIcon.getImage().getScaledInstance(700, 350, Image.SCALE_SMOOTH);
        JLabel backgroundLabel = new JLabel(new ImageIcon(scaledImage));
        backgroundLabel.setLayout(new GridBagLayout());


        // === Text Overlay Panel ===
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(true);
        textPanel.setBackground(new Color(255, 255, 255, 200)); // White semi-transparent

        // Add components to textPanel
        jLabel2.setAlignmentX(Component.LEFT_ALIGNMENT);
        jLabel3.setAlignmentX(Component.LEFT_ALIGNMENT);
        jLabel5.setAlignmentX(Component.LEFT_ALIGNMENT);
        jLabel4.setAlignmentX(Component.LEFT_ALIGNMENT);
        jButton1.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        textPanel.add(jLabel2);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(jLabel3);
        textPanel.add(jLabel5);
        textPanel.add(Box.createVerticalStrut(20));
        textPanel.add(jLabel4);
        textPanel.add(Box.createVerticalStrut(20));
        textPanel.add(jButton1);

        backgroundLabel.add(textPanel); // Add text panel on top of background

        // === Frame Layout ===
        setLayout(new BorderLayout());
        add(topNav, BorderLayout.NORTH);
        add(backgroundLabel, BorderLayout.CENTER);

        // === Button Actions ===
        jButton1.addActionListener(evt -> {
            CustomerOrderUI a = new CustomerOrderUI(username, userType);
            a.setVisible(true);
            this.setVisible(false);
        });

        jButton2.addActionListener(evt -> {
            ProfileEdit edit = new ProfileEdit(username, userType);
            edit.setVisible(true);
            this.setVisible(false);
        });

        logout.addActionListener(evt -> {
            Login a = new Login();
            a.setVisible(true);
            this.setVisible(false);
        });

        setTitle("Customer Home");
        setSize(700, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //test main function to run directly 
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new CustomerHome2("Jet", "customer").setVisible(true);
        });
    }
}
