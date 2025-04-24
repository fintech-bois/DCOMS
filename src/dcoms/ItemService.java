package dcoms;

import java.rmi.*;
import java.util.List;
import java.util.Map;

public interface ItemService extends Remote {
    public List<Item> getAllItems() throws RemoteException;
    public Item getItemById(int itemId) throws RemoteException;
    public void placeOrder(Map<Integer, Integer> orderItems, double totalAmount, String username) throws RemoteException;
}
