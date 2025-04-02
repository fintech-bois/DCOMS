/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcoms;
import java.rmi.*;
import java.util.List;

/**
 *
 * @author yipzh
 */
public interface ReportService extends Remote {
    public void getItemDetails()throws RemoteException;
    public void getOrderItemDetails()throws RemoteException;
    public void calculateTotalAmount()throws RemoteException;
    public List<Sale> getReportDetails()throws RemoteException;
}
