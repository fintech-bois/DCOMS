/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcoms;
import java.rmi.*;
/**
 *
 * @author yipzh
 */
public interface UserService extends Remote{
    public String authenticateUser(String username, String password)throws RemoteException;
    public String signup(String username, String password, String fname, String lname, String ic, String userType) throws RemoteException;
    
    // Get user details
    public String[] getUserDetails(String username) throws RemoteException;

    // Update profile
    public String updateProfile(String username, String password, String fname, String lname, String ic) throws RemoteException;
    
    boolean usernameExists(String username) throws RemoteException;

}
