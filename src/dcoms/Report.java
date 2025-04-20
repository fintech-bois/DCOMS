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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yipzh
 */
public class Report extends UnicastRemoteObject implements ReportService {
    String url = "jdbc:derby://localhost:1527/FBOSdb", dbUser = "test", dbPass = "test", query;
    List<Sale> salesList = new ArrayList<>();
    private Date fromDate, toDate;
    PreparedStatement ps = null;
    
    public Report()throws RemoteException
    {
        super();
    }
    
    public void unfilteredQuery() throws RemoteException {
        try {
            query = "SELECT i.itemID, i.itemName, i.price, " +
                    "COALESCE(SUM(oi.quantity), 0) AS totalQuantity, " +
                    "COALESCE(SUM(oi.quantity * i.price), 0) AS totalAmount " +
                    "FROM Items i " +
                    "LEFT JOIN Order_Items oi ON i.itemID = oi.itemID " +
                    "LEFT JOIN Orders o ON oi.orderID = o.orderID " +
                    "GROUP BY i.itemID, i.itemName, i.price";
            
            Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
            ps = conn.prepareStatement(query);
        } catch (SQLException ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void filteredQuery() throws RemoteException {
        try {
            query = "SELECT i.itemID, i.itemName, i.price, " +
                    "COALESCE(SUM(oi.quantity), 0) AS totalQuantity, " +
                    "COALESCE(SUM(oi.quantity * i.price), 0) AS totalAmount " +
                    "FROM Items i " +
                    "LEFT JOIN Order_Items oi ON i.itemID = oi.itemID " +
                    "LEFT JOIN Orders o ON oi.orderID = o.orderID " +
                    "WHERE DATE(o.orderDate) BETWEEN ? AND ? " +
                    "GROUP BY i.itemID, i.itemName, i.price";
            
            Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
            ps = conn.prepareStatement(query);
            
            java.sql.Timestamp sqlFrom = new java.sql.Timestamp(fromDate.getTime());
            java.sql.Timestamp sqlTo = new java.sql.Timestamp(toDate.getTime());
            ps.setTimestamp(1, sqlFrom);
            ps.setTimestamp(2, sqlTo);
        } catch (SQLException ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void fetchSalesDetails() throws RemoteException {
        salesList.clear(); // Clear previous data before adding new ones
        
        try {
            ResultSet rs = ps.executeQuery();

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
    
    @Override
    public void setFilterDate(Date fromDate, Date toDate) throws RemoteException {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
}