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
    
    @Override
    public void fetchSalesDetails() throws RemoteException {
    String query = "SELECT i.itemID, i.itemName, i.price, " +
                   "COALESCE(SUM(oi.quantity), 0) AS totalQuantity, " +
                   "COALESCE(SUM(oi.quantity * i.price), 0) AS totalAmount " +
                   "FROM Items i " +
                   "LEFT JOIN Order_Items oi ON i.itemID = oi.itemID " +
                   "GROUP BY i.itemID, i.itemName, i.price";

    salesList.clear();  // Clear previous data before adding new ones

    try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            int itemID = rs.getInt("itemID");
            String itemName = rs.getString("itemName");
            double price = rs.getDouble("price");
            int quantity = rs.getInt("totalQuantity");
            double totalAmount = rs.getDouble("totalAmount");

            salesList.add(new Sale(itemID, itemName, price, quantity, totalAmount));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    @Override
    public List<Sale> getReportDetails()throws RemoteException{
        fetchSalesDetails();
        return salesList;
    }
}