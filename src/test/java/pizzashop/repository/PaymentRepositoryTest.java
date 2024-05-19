package pizzashop.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentRepositoryTest {

    PaymentRepository repository;

    private final LocalDateTime dateTime = LocalDateTime.of(2024, 10, 10, 10, 10, 10);

    private final String testFilePath = "data/payments_test.txt";

    @BeforeEach
    void setUp() {
        repository = new PaymentRepository(testFilePath);
    }

    @AfterEach
    void tearDown() {
        try (FileWriter fw = new FileWriter(testFilePath)) {
            fw.write("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAddPaymentSuccessful() {
        Payment payment1 = getMockPayment(3, PaymentType.CASH, 33.3, dateTime);
        Payment payment2 = getMockPayment(3, PaymentType.CARD, 33.3, dateTime);

        repository.add(payment1.getTableNumber(), payment1.getType(), payment1.getAmount(), payment1.getOrderPlacedAt());
        repository.add(payment2.getTableNumber(), payment2.getType(), payment2.getAmount(), payment2.getOrderPlacedAt());

        List<Payment> paymentsList = repository.getAll();

        assertEquals(2, paymentsList.size());
        assertEquals(payment1.getTableNumber(), paymentsList.get(0).getTableNumber());
        assertEquals(payment1.getType(), paymentsList.get(0).getType());
        assertEquals(payment1.getAmount(), paymentsList.get(0).getAmount());
        assertEquals(payment1.getOrderPlacedAt(), paymentsList.get(0).getOrderPlacedAt());
    }

    @Test
    void testEmptyGetAll() {
        assertEquals(0,repository.getAll().size());
    }

    Payment getMockPayment(int tableNumber, PaymentType type, double amount, LocalDateTime dateTime) {
        Payment payment = Mockito.mock(Payment.class);

        Mockito.when(payment.getTableNumber()).thenReturn(tableNumber);
        Mockito.when(payment.getType()).thenReturn(type);
        Mockito.when(payment.getAmount()).thenReturn(amount);
        Mockito.when(payment.getOrderPlacedAt()).thenReturn(dateTime);

        return payment;
    }
}