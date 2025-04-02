/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcoms;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
/**
 *
 * @author yipzh
 */
public class Register {
    public static void main(String args[])throws RemoteException
    {
        Registry reg = LocateRegistry.createRegistry(1099);
        reg.rebind("Users", new Users());
        reg.rebind("Report", new Report());
    }
}
