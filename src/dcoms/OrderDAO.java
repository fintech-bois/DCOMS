/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcoms;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 *
 * @author yipzh
 */
public class OrderDAO {
    public void insertOrders(Map<Integer, Integer> orderItems, double totalAmount, String username) throws RemoteException, SQLException {
        String selectQuery = "SELECT userID FROM USERS WHERE username = ?";
        String insertOrderQuery = "INSERT INTO Orders (userID, totalAmount) VALUES (?, ?)";
        String insertItemQuery = 
            "INSERT INTO ORDER_ITEMS (orderID, itemID, quantity, subtotal) " +
            "SELECT ?, ?, ?, i.price * ? AS subtotal FROM ITEMS i WHERE i.itemID = ?";

        try (Connection conn = DBConnection.getInstance().createConnection()) {
            conn.setAutoCommit(false); // Begin transaction

            int userID = 0;
            try (PreparedStatement ps = conn.prepareStatement(selectQuery)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        userID = rs.getInt("userID");
                    }
                }
            }

            int orderID = 0;
            try (PreparedStatement ps2 = conn.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS)) {
                ps2.setInt(1, userID);
                ps2.setDouble(2, totalAmount);
                int affectedRows = ps2.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = ps2.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            orderID = generatedKeys.getInt(1);
                        }
                    }
                }
            }

            try (PreparedStatement pst = conn.prepareStatement(insertItemQuery)) {
                for (Map.Entry<Integer, Integer> entry : orderItems.entrySet()) {
                    int itemID = entry.getKey();
                    int quantity = entry.getValue();

                    pst.setInt(1, orderID);
                    pst.setInt(2, itemID);
                    pst.setInt(3, quantity);
                    pst.setInt(4, quantity); // for price * quantity
                    pst.setInt(5, itemID);
                    pst.executeUpdate();
                }
            }

            conn.commit(); // Commit transaction

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
