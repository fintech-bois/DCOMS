/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcoms;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yipzh
 */
public class Report extends UnicastRemoteObject implements ReportService {
    String url = "jdbc:derby://localhost:1527/FBOSdb", dbUser = "test", dbPass = "test";
    List<Sale> salesList = new ArrayList<>();
    
    public Report()throws RemoteException
    {
        super();
    }
    
    public void getItemDetails()throws RemoteException{
        String query = "SELECT * FROM Items";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                int itemID = rs.getInt("itemID");
                String itemName = rs.getString("itemName");
                double price = rs.getDouble("price");
                salesList.add(new Sale(itemID, itemName, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void getOrderItemDetails()throws RemoteException{
        salesList.forEach((sale) -> {
            int itemID = sale.itemID;
            String query = "SELECT * FROM Order_Items WHERE itemID = ?";
            try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
                    PreparedStatement pst = conn.prepareStatement(query)) {
                
                pst.setInt(1, itemID);
                ResultSet rs = pst.executeQuery();
                int quantity = 0;
                while (rs.next()) {
                    quantity = quantity + rs.getInt("quantity");
                }
                sale.quantity = quantity;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    
    public void calculateTotalAmount()throws RemoteException{
        salesList.forEach((sale) -> {
            sale.totalAmount = sale.price * sale.quantity;
        });
    }
    
    public List<Sale> getReportDetails()throws RemoteException{
        salesList.clear();
        getItemDetails();
        getOrderItemDetails();
        calculateTotalAmount();
        return salesList;
    }
}