/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcoms;

import java.io.Serializable;

/**
 *
 * @author yipzh
 */
public class Sale implements Serializable {
    int itemID;
    String itemName;
    double price;
    int quantity;
    double totalAmount;

    public Sale(int itemID, String itemName, double price) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.price = price;
    }
}