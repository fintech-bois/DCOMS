/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcoms;
import java.rmi.*;
import java.util.Date;
import java.util.List;

/**
 *
 * @author yipzh
 */
public interface ReportService extends Remote {
    public void fetchSalesDetails()throws RemoteException;
    public List<Sale> getReportDetails()throws RemoteException;
    public void setFilterDate(Date fromDate, Date toDate) throws RemoteException;
    public void unfilteredQuery() throws RemoteException;
    public void filteredQuery() throws RemoteException;
}
