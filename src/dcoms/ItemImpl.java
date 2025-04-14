package dcoms;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ItemImpl extends UnicastRemoteObject implements ItemService {
    String url = "jdbc:derby://localhost:1527/FBOSdb", dbUser = "test", dbPass = "test";
    
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
}
