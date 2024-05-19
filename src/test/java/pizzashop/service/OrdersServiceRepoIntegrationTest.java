package pizzashop.service;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.service.OrdersService;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class OrdersServiceRepoIntegrationTest {

    private final LocalDateTime dateTime = LocalDateTime.of(2024, 10, 10, 10, 10, 10);
    private final String testFilePath = "data/payments_test.txt";

    @Mock
    public MenuRepository menuRepository;

    @Mock
    public PaymentRepository paymentRepository;

    @InjectMocks
    public OrdersService ordersService;

    @BeforeEach
    void setUp() {
        menuRepository = Mockito.mock(MenuRepository.class);
        paymentRepository = new PaymentRepository(testFilePath);
        ordersService = new OrdersService(menuRepository, paymentRepository);
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
    public void addPaymentSuccessfully() {
        Payment payment = getMockPayment(3, PaymentType.CARD, 15, dateTime);

        ordersService.addPayment(payment.getTableNumber(), payment.getType(), payment.getAmount());

        Payment addedPayment = paymentRepository.getAll().get(0);

        assertEquals(payment.getTableNumber(), addedPayment.getTableNumber());
        assertEquals(payment.getType(), addedPayment.getType());
        assertEquals(payment.getAmount(), addedPayment.getAmount());
        assertNotNull(addedPayment.getOrderPlacedAt());
    }

    @Test
    public void addPaymentWithNegativeAmount() {
        Payment payment = getMockPayment(3, PaymentType.CARD, -1, dateTime);

        assertThrows(
                RuntimeException.class,
                () -> ordersService.addPayment(payment.getTableNumber(), payment.getType(), payment.getAmount()),
                "Invalid amount");
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