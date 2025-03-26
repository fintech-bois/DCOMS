/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcoms;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
/**
 *
 * @author yipzh
 */
public class Server extends UnicastRemoteObject implements rmiinterface{
    double pi = 3.142;
    public Server()throws RemoteException
    {
        super();
    }
    @Override
    public int add(int x, int y)throws RemoteException
    {
        return x*y;
    }
    public double areaOfCircle(double r)throws RemoteException {
        double area = pi * r * r;
        return area;
    }
}
