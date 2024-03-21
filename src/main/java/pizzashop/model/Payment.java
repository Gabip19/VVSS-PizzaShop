package pizzashop.model;


import java.time.LocalDateTime;

public class Payment {
    private int tableNumber;
    private PaymentType type;
    private double amount;
    private LocalDateTime orderPlacedAt;

    public Payment(int table, PaymentType type, double amount, LocalDateTime dateTime) {
        this.tableNumber = table;
        this.type = type;
        this.amount = amount;
        this.orderPlacedAt = dateTime;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return tableNumber + "," + type + "," + amount + "," + orderPlacedAt.toString();
    }

    public LocalDateTime getOrderPlacedAt() {
        return orderPlacedAt;
    }

    public void setOrderPlacedAt(LocalDateTime orderPlacedAt) {
        this.orderPlacedAt = orderPlacedAt;
    }
}
