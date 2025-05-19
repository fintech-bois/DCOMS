package dcoms;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemImpl extends UnicastRemoteObject implements ItemService {
    String url = "jdbc:derby://localhost:1527/FBOSdb", dbUser = "test", dbPass = "test";
    
    Map<Integer, Integer> orderItems;
    int orderID;
    
    public ItemImpl() throws RemoteException {
        super();
    }
    
        @Override
    public void addItem(Item item) throws RemoteException {
        String query = "INSERT INTO Items (itemName, category, price, image) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, item.getItemName());
            ps.setString(2, item.getCategory());
            ps.setDouble(3, item.getPrice());
            ps.setString(4, item.getImage());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error adding item", e);
        }
    }

    @Override
    public void updateItem(Item item) throws RemoteException {
        String query = "UPDATE Items SET itemName=?, category=?, price=?, image=? WHERE itemID=?";
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, item.getItemName());
            ps.setString(2, item.getCategory());
            ps.setDouble(3, item.getPrice());
            ps.setString(4, item.getImage());
            ps.setInt(5, item.getItemId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error updating item", e);
        }
    }

    @Override
    public void deleteItem(int itemId) throws RemoteException {
        String query = "DELETE FROM Items WHERE itemID=?";
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, itemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error deleting item", e);
        }
    }

    
    @Override
    public List<Item> getAllItems() throws RemoteException {
        List<Item> itemsList = new ArrayList<>();
        String query = "SELECT itemID, itemName, category, price, image FROM Items";
        
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                int itemId = rs.getInt("itemID");
                String itemName = rs.getString("itemName");
                String category = rs.getString("category");
                double price = rs.getDouble("price");
                String image = rs.getString("image");
                
                itemsList.add(new Item(itemId, itemName, category, price, image));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Database error while fetching items", e);
        }
        
        return itemsList;
    }
    
    @Override
    public Item getItemById(int itemId) throws RemoteException {
        String query = "SELECT itemID, itemName, category, price, image FROM Items WHERE itemID = " + itemId;
        
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                String itemName = rs.getString("itemName");
                String category = rs.getString("category");
                double price = rs.getDouble("price");
                String image = rs.getString("image");
                
                return new Item(itemId, itemName, category, price, image);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Database error while fetching item", e);
        }
        
        return null; // Item not found
    }
    
    @Override
    public void placeOrder(Map<Integer, Integer> orderItems, double totalAmount, String username) throws RemoteException {
        new Thread(() -> {
            try {
                OrderDAO dao = new OrderDAO(); // your DAO class
                dao.insertOrders(orderItems, totalAmount, username);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (SQLException ex) {
                Logger.getLogger(ItemImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start(); // Run in a new thread
    }
}
