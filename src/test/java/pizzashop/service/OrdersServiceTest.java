package pizzashop.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class OrdersServiceTest {

    private final LocalDateTime dateTime = LocalDateTime.of(2024, 10, 10, 10, 10, 10);

    @Mock
    public MenuRepository menuRepository;

    @Mock
    public PaymentRepository paymentRepository;

    @InjectMocks
    public OrdersService ordersService;

    @BeforeEach
    void setUp() {
        ordersService = new OrdersService(menuRepository, paymentRepository);
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void tearDown() {
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

        Mockito.verify(paymentRepository).add(
            eq(payment.getTableNumber()),
            eq(payment.getType()),
            eq(payment.getAmount()),
            any(LocalDateTime.class));
        verifyNoMoreInteractions(paymentRepository);
    }

    @Test
    @Order(2)
    @DisplayName("Test for negative table number - TC2_ECP")
    @Tag("TC2_ECP")
    public void addPaymentWithInvalidTableNoECP() {
        Payment payment = new Payment(-3, PaymentType.CASH, 15, dateTime);

        assertThrows(
            RuntimeException.class,
            () -> ordersService.addPayment(payment.getTableNumber(), payment.getType(), payment.getAmount()),
            "Invalid table id");
        verifyNoInteractions(paymentRepository);
    }

    @Test
    @Order(3)
    @DisplayName("Test for 0 table number - TC1_BVA")
    @Tag("TC1_BVA")
    public void addPaymentWith0TableNo() {
        Payment payment = new Payment(0, PaymentType.CARD, 15, dateTime);

        assertThrows(
            RuntimeException.class,
            () -> ordersService.addPayment(payment.getTableNumber(), payment.getType(), payment.getAmount()),
            "Invalid table id");
        verifyNoInteractions(paymentRepository);
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
        verifyNoInteractions(paymentRepository);
    }

    @Test
    @Order(5)
    @DisplayName("Test for minimum amount 0.1 - TC9_BVA")
    @Tag("TC9_BVA")
    public void addPaymentWithMinimumAmount() {
        Payment payment = new Payment(3, PaymentType.CASH, 0.1, dateTime);

        ordersService.addPayment(payment.getTableNumber(), payment.getType(), payment.getAmount());

        Mockito.verify(paymentRepository).add(
                eq(payment.getTableNumber()),
                eq(payment.getType()),
                eq(payment.getAmount()),
                any(LocalDateTime.class));
        verifyNoMoreInteractions(paymentRepository);
    }

    @Test
    @Order(6)
    @DisplayName("Test for maximum table number 8 - TC5_BVA")
    @Tag("TC5_BVA")
    public void addPaymentWithMaximumTableNo() {
        Payment payment = new Payment(8, PaymentType.CARD, 20, dateTime);

        ordersService.addPayment(payment.getTableNumber(), payment.getType(), payment.getAmount());

        Mockito.verify(paymentRepository).add(
                eq(payment.getTableNumber()),
                eq(payment.getType()),
                eq(payment.getAmount()),
                any(LocalDateTime.class));
        verifyNoMoreInteractions(paymentRepository);
    }
}