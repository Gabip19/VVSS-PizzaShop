package pizzashop.integration;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

public class OrdersServiceFullIntegrationTest {

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

    @ParameterizedTest
    @Order(1)
    @DisplayName("Test for checking a payment - TC1_ECP | TC5_ECP")
    @Tag("TC1_ECP")
    @Tag("TC5_ECP")
    @ValueSource(doubles = {15, 20})
    public void addPaymentSuccessfully(double amount) {
        Payment payment = new Payment(3, PaymentType.CARD, amount, dateTime);

        ordersService.addPayment(payment.getTableNumber(), payment.getType(), payment.getAmount());

        Payment addedPayment = paymentRepository.getAll().get(0);

        assertEquals(payment.getTableNumber(), addedPayment.getTableNumber());
        assertEquals(payment.getType(), addedPayment.getType());
        assertEquals(payment.getAmount(), addedPayment.getAmount());
        assertNotNull(addedPayment.getOrderPlacedAt());
    }

    @Test
    @Order(4)
    @Timeout(1)
    @DisplayName("Test for negative amount - TC7_BVA")
    @Tag("TC7_BVA")
    public void addPaymentWithNegativeAmount() {
        Payment payment = new Payment(3, PaymentType.CARD, -1, dateTime);

        assertThrows(
                RuntimeException.class,
                () -> ordersService.addPayment(payment.getTableNumber(), payment.getType(), payment.getAmount()),
                "Invalid amount");
    }
}