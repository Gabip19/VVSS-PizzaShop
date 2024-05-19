package pizzashop.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    @Test
    void getType() {
        Payment payment = new Payment(1, PaymentType.CARD, 12.2, LocalDateTime.of(2024, 1, 1, 1, 0));
        assert payment.getType() == PaymentType.CARD;
    }

    @Test
    void setType() {
        Payment payment = new Payment(1, PaymentType.CASH, 12.2, LocalDateTime.of(2024, 1, 1, 1, 0));
        payment.setType(PaymentType.CARD);
        assert payment.getType() == PaymentType.CARD;
    }
}