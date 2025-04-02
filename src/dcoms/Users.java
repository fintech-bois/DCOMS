/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dcoms;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author yipzh
 */
public class Users extends UnicastRemoteObject implements UserService {
    String url = "jdbc:derby://localhost:1527/FBOSdb", dbUser = "test", dbPass = "test";
    
    public Users()throws RemoteException
    {
        super();
    }
    
    public String authenticateUser(String username, String password)throws RemoteException {
        String userType = null;
        String query = "SELECT userType FROM Users WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
            PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) { // If a row is found, get usertype
                userType = rs.getString("userType");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userType;
    }
    
    public String signup(String username, String password, String fname, String lname, String ic)throws RemoteException{
        String query = "INSERT INTO Users (username, password, fname, lname, ic) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, fname);
            stmt.setString(4, lname);
            stmt.setString(5, ic);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                return "Successful";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error: " + ex.getMessage());
        }
        return "Unsuccessful";
    }
    
//    public String getCurrentDateTime(){
//        // Get the current date and time
//        LocalDateTime currentDateTime = LocalDateTime.now();
//
//        // Define a custom format
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        // Format the LocalDateTime object
//        String formattedDateTime = currentDateTime.format(formatter);
//        
//        return formattedDateTime;
//    }
}
