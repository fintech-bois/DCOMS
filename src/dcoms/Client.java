/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcoms;
import java.rmi.*;
import java.net.MalformedURLException;
import java.util.Scanner;
/**
 *
 * @author yipzh
 */
public class Client {
    public static void main (String args[])throws RemoteException, NotBoundException, MalformedURLException
    {
        rmiinterface Obj = (rmiinterface)Naming.lookup("rmi://localhost:1099/add");  //malformedurlexception //localhost change to IP for distributed
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter 1st integer: ");
        int int1 = scanner.nextInt();
        System.out.print("Enter 2nd integer: ");
        int int2 = scanner.nextInt();
        System.out.println("The number is: " + Obj.add(int1, int2));
        
        // Read a double
        System.out.print("Enter radius of circle: ");
        double doubleValue = scanner.nextDouble();
        System.out.println("The area of the circle is: " + Obj.areaOfCircle(doubleValue));
    }
}
