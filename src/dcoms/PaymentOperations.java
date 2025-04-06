package dcoms;

import javax.swing.*;
import java.util.ArrayList;

public class PaymentOperations {
    private ArrayList<OrderItem> orderItems;
    private double totalAmount;
    
    // Class to represent order items
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
    
    public PaymentOperations() {
        orderItems = new ArrayList<>();
        totalAmount = 0.0;
    }
    
    // Calculate the total amount from all items
    public void calculateTotal() {
        totalAmount = 0.0;
        for (OrderItem item : orderItems) {
            totalAmount += item.getSubtotal();
        }
    }
    
    // Add an item to the order
    public void addItem(String name, double price, int quantity) {
        // Check if the item already exists in cart
        for (int i = 0; i < orderItems.size(); i++) {
            OrderItem item = orderItems.get(i);
            if (item.name.equals(name) && Math.abs(item.price - price) < 0.001) {
                // Update quantity instead of adding a new item
                item.quantity += quantity;
                calculateTotal();
                return;
            }
        }
        
        // If item doesn't exist, add as new
        orderItems.add(new OrderItem(name, price, quantity));
        calculateTotal();
    }
    
    // Remove item safely using this method
    public void removeItemSafely(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < orderItems.size()) {
            boolean isLastItem = orderItems.size() == 1;
            orderItems.remove(rowIndex);
            calculateTotal();
            
            if (isLastItem) {
                JOptionPane.showMessageDialog(
                    null,
                    "Your cart is now empty. Please add items to continue.",
                    "Empty Cart",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }
    
    // Update the quantity of an existing item
    public void updateItemQuantity(int index, int newQuantity) {
        if (index >= 0 && index < orderItems.size() && newQuantity > 0) {
            orderItems.get(index).quantity = newQuantity;
            calculateTotal();
        }
    }
    
    // Process payment for the order
    public boolean processPayment(String paymentMethod) {
        if (orderItems.isEmpty()) {
            return false;
        }
        
        // In a real application, this would handle actual payment processing logic
        System.out.println("Processing payment of $" + String.format("%.2f", totalAmount) + 
                          " using " + paymentMethod);
        
        // For this demo, we'll just return true to simulate successful payment
        return true;
    }
 
    public void clearCart() {
        orderItems.clear();
        totalAmount = 0.0;
    }
    
    // Decrement item quantity by 1 
    public void decrementItemQuantity(int index) {
        if (index >= 0 && index < orderItems.size()) {
            OrderItem item = orderItems.get(index);
            
            if (item.quantity > 1) {
                updateItemQuantity(index, item.quantity - 1);
            } else {
                removeItemSafely(index);
            }
        }
    }
    
    // Complete order process (moved from PaymentUI)
    public boolean completeOrder(String paymentMethod) {
        if (isCartEmpty()) {
            return false;
        }    
        boolean paymentSuccess = processPayment(paymentMethod);        
        if (paymentSuccess) {
            clearCart();
        }
        
        return paymentSuccess;
    }
    
    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }

    public boolean isCartEmpty() {
        return orderItems.isEmpty();
    }
}