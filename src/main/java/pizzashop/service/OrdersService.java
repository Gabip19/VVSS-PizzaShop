package pizzashop.service;

import pizzashop.model.MenuItem;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class OrdersService {
    private MenuRepository menuRepo;
    private PaymentRepository payRepo;

    public OrdersService(MenuRepository menuRepo, PaymentRepository payRepo){
        this.menuRepo=menuRepo;
        this.payRepo=payRepo;
    }

    public List<MenuItem> getMenuData(){return menuRepo.getMenu();}

    public List<Payment> getPayments(){return payRepo.getAll(); }

    public void addPayment(int table, PaymentType type, double amount) {
        if (table < 1 || table > 8) {
            throw new RuntimeException("Invalid table id");
        }

        if (amount <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        LocalDateTime currentDate = LocalDateTime.now();
        payRepo.add(table, type, amount, currentDate);
    }

    public double getTotalPaymentsAmountForDate(PaymentType type, LocalDate date){
        double total = 0.0d;
        List<Payment> l = getPayments();

        if (l == null || l.isEmpty())
            return total;

        for (Payment p : l) {
            if (p.getType().equals(type) && p.getOrderPlacedAt().toLocalDate().isEqual(date))
                total += p.getAmount();
        }

        return total;
    }

}