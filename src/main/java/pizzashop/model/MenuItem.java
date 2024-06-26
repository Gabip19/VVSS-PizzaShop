package pizzashop.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MenuItem  {
    private final SimpleStringProperty menuItemName;
    private final SimpleIntegerProperty quantity;

    private final SimpleDoubleProperty price;

    public MenuItem(String mItem, Integer mQuantity, Double mPrice) {
        this.menuItemName = new SimpleStringProperty(mItem);
        this.quantity = new SimpleIntegerProperty(mQuantity);
        this.price = new SimpleDoubleProperty(mPrice);
    }

    public String getMenuItem() {
        return menuItemName.get();
    }

    public SimpleStringProperty menuItemProperty() {
        return menuItemName;
    }

    public void setMenuItem(String menuItem) {
        this.menuItemName.set(menuItem);
    }

    public Integer getQuantity() {
        return quantity.get();
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity.set(quantity);
    }

    public Double getPrice() {
        return price.get();
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(Double price) {
        this.price.set(price);
    }
}