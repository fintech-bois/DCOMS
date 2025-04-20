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

public class ItemImpl extends UnicastRemoteObject implements ItemService {
    String url = "jdbc:derby://localhost:1527/FBOSdb", dbUser = "test", dbPass = "test";
    
    Map<Integer, Integer> orderItems;
    int orderID;
    
    public ItemImpl() throws RemoteException {
        super();
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
    public void insertOrders(Map<Integer, Integer> orderItems, double totalAmount) throws RemoteException {
        String insertQuery = "INSERT INTO Orders (userID, totalAmount) VALUES (?, ?)";
        try (Connection conn = DBConnection.getInstance().createConnection();
            PreparedStatement ps = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, Session.getInstance().getUserID());
            ps.setDouble(2, totalAmount);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        orderID = generatedKeys.getInt(1);  // Get the auto-generated PK
                        this.orderItems = orderItems;
                        insertOrderItems();
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void insertOrderItems() throws RemoteException {
        String insertQuery = 
            "INSERT INTO ORDER_ITEMS (orderID, itemID, quantity, subtotal) " +
            "SELECT ?, ?, ?, i.price * ? AS subtotal " +
            "FROM ITEMS i " +
            "WHERE i.itemID = ?";

        try (Connection conn = DBConnection.getInstance().createConnection();
             PreparedStatement pst = conn.prepareStatement(insertQuery)) {

            for (Map.Entry<Integer, Integer> entry : orderItems.entrySet()) {
                int itemID = entry.getKey();
                int quantity = entry.getValue();

                pst.setInt(1, orderID);
                pst.setInt(2, itemID);
                pst.setInt(3, quantity);
                pst.setInt(4, quantity);  // used for price * quantity
                pst.setInt(5, itemID);
                pst.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
