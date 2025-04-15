
package dcoms;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Register {
    public static void main(String args[])throws RemoteException
    {
        Registry reg = LocateRegistry.createRegistry(1099);
        reg.rebind("Users", new Users());
        reg.rebind("Report", new Report());
        reg.rebind("ItemService", new ItemImpl());
    }
}
