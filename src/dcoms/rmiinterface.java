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
    public int add(int x, int y)throws RemoteException;
    public double areaOfCircle(double r)throws RemoteException;
}
