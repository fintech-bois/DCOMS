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
public interface rmiinterface extends Remote{
    public String authenticateUser(String username, String password)throws RemoteException;
    public String signup(String username, String password, String fname, String lname, String ic)throws RemoteException;
}
