package dcoms;

import java.rmi.*;
import java.util.List;
import java.util.Map;

public interface ItemService extends Remote {
    public List<Item> getAllItems() throws RemoteException;
    public Item getItemById(int itemId) throws RemoteException;
    public void insertOrders(Map<Integer, Integer> orderItems, double totalAmount) throws RemoteException;
    public void insertOrderItems() throws RemoteException;
}
