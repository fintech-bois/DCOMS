package dcoms;

import java.io.Serializable;

public class Item implements Serializable {
    private int itemId;
    private String itemName;
    private String category;
    private double price;
    private String image; 
    
    public Item(int itemId, String itemName, String category, double price, String image) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.category = category;
        this.price = price;
        this.image = image;
    }
    
    // Getters and setters method
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public String getImage() { return image;}
    public void setImage(String image) { this.image = image; }

    @Override
    public String toString() {
        return itemName + " - $" + price;
    }
}
