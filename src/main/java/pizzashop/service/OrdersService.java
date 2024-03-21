package pizzashop.service;

import pizzashop.model.MenuItem;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

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

    public void addPayment(int table, PaymentType type, double amount){
        LocalDateTime currentDate = LocalDateTime.now();
        payRepo.add(table, type, amount, currentDate);
    }

    public double getTotalAmount(PaymentType type){
        double total=0.0d;
        List<Payment> l=getPayments();
        if ((l==null) ||(l.size()==0)) return total;
        for (Payment p:l){
            if (p.getType().equals(type))
                total+=p.getAmount();
        }
        return total;
    }

}