package dcoms;

import java.rmi.*;
import java.util.List;

public interface ItemService extends Remote {
    public List<Item> getAllItems() throws RemoteException;
    public Item getItemById(int itemId) throws RemoteException;

}
