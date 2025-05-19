package dcoms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.util.List;

public class AdminUI extends JFrame {
    private ItemService itemService;
    private JTable table;
    private DefaultTableModel tableModel;


    private String username;
    private String userType;


    public AdminUI(String username, String userType) {
        this.username = username;
        this.userType = userType;

        setTitle("Admin - Manage Items");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        connectToRMI();
        initComponents();
        loadItems();
    }

    private void connectToRMI() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            itemService = (ItemService) registry.lookup("ItemService");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to connect to RMI: " + e.getMessage());
            System.exit(1);
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Category", "Price", "Image"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();

        JButton addBtn = new JButton("Add Item");
        JButton editBtn = new JButton("Edit Item");
        JButton deleteBtn = new JButton("Delete Item");
        JButton backBtn = new JButton("Back");

        addBtn.addActionListener(e -> showItemDialog(null));
        editBtn.addActionListener(e -> editSelectedItem());
        deleteBtn.addActionListener(e -> deleteSelectedItem());
        backBtn.addActionListener(e -> {
            dispose();
            new AdminHome(username, userType).setVisible(true);
        });

        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(backBtn);

        add(btnPanel, BorderLayout.SOUTH);
    }

    private void loadItems() {
        try {
            List<Item> items = itemService.getAllItems();
            tableModel.setRowCount(0); 
            for (Item item : items) {
                tableModel.addRow(new Object[]{
                        item.getItemId(),
                        item.getItemName(),
                        item.getCategory(),
                        item.getPrice(),
                        item.getImage()
                });
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Failed to load items: " + e.getMessage());
        }
    }


    private void showItemDialog(Item existingItem) {
    JTextField nameField = new JTextField();
    String[] categories = {"Food", "Beverage"};
    JComboBox<String> categoryComboBox = new JComboBox<>(categories);

    JTextField priceField = new JTextField();
    JTextField imageField = new JTextField();
    JButton browseButton = new JButton("Browse...");

    JPanel imagePanel = new JPanel(new BorderLayout());
    imagePanel.add(imageField, BorderLayout.CENTER);
    imagePanel.add(browseButton, BorderLayout.EAST);

    browseButton.addActionListener(e -> {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            imageField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    });

    if (existingItem != null) {
        nameField.setText(existingItem.getItemName());
        categoryComboBox.setSelectedItem(existingItem.getCategory());
        priceField.setText(String.valueOf(existingItem.getPrice()));
        imageField.setText(existingItem.getImage());
    }

    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("Name:"));
    panel.add(nameField);
    panel.add(new JLabel("Category:"));
    panel.add(categoryComboBox); 
    panel.add(new JLabel("Price:"));
    panel.add(priceField);
    panel.add(new JLabel("Image File Path:"));
    panel.add(imagePanel);

    int result = JOptionPane.showConfirmDialog(this, panel,
            existingItem == null ? "Add Item" : "Edit Item", JOptionPane.OK_CANCEL_OPTION);

    if (result == JOptionPane.OK_OPTION) {
        try {
            Item item = new Item(
                    existingItem == null ? 0 : existingItem.getItemId(),
                    nameField.getText(),
                    (String) categoryComboBox.getSelectedItem(), 
                    Double.parseDouble(priceField.getText()),
                    imageField.getText()
            );
            if (existingItem == null) {
                itemService.addItem(item);
            } else {
                itemService.updateItem(item);
            }
            loadItems();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}


    private void editSelectedItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                Item item = itemService.getItemById(id);
                showItemDialog(item);
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(this, "Failed to fetch item details.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select an item to edit.");
        }
    }

    private void deleteSelectedItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    itemService.deleteItem(id);
                    loadItems();
                } catch (RemoteException e) {
                    JOptionPane.showMessageDialog(this, "Failed to delete item.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select an item to delete.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminUI("admin", "admin").setVisible(true));
    }
}
