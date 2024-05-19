package pizzashop.service;

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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class OrdersServiceTest {

    private final LocalDateTime dateTime = LocalDateTime.of(2024, 10, 10, 10, 10, 10);

    @Mock
    public MenuRepository menuRepository;

    @Mock
    public PaymentRepository paymentRepository;

    @InjectMocks
    public OrdersService ordersService;

    @BeforeEach
    void setUp() {
        menuRepository = Mockito.mock(MenuRepository.class);
        paymentRepository = Mockito.mock(PaymentRepository.class);
        ordersService = new OrdersService(menuRepository, paymentRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Order(1)
    @DisplayName("Test for checking a payment - TC1_ECP | TC5_ECP")
    @Tag("TC1_ECP")
    @Tag("TC5_ECP")
    public void addPaymentSuccessfully() {
        Payment payment = new Payment(3, PaymentType.CARD, 15, dateTime);

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

    @Test
    @Order(7)
    public void getTotalPaymentsWithNullList() {
        Mockito.when(paymentRepository.getAll()).thenReturn(null);

        double total = ordersService.getTotalPaymentsAmountForDate(PaymentType.CASH, LocalDate.of(2024, 4, 18));

        assert total == 0;
    }

    @Test
    @Order(8)
    public void getTotalPaymentsWithEmptyList() {
        Mockito.when(paymentRepository.getAll()).thenReturn(Collections.emptyList());

        double total = ordersService.getTotalPaymentsAmountForDate(PaymentType.CASH, LocalDate.of(2024, 4, 18));

        assert total == 0;
    }

    @Test
    @Order(9)
    public void getTotalPaymentsWithDifferentPaymentType() {
        Mockito.when(paymentRepository.getAll()).thenReturn(new ArrayList<>(
            List.of(new Payment[]{new Payment(1, PaymentType.CARD, 100, LocalDateTime.of(2024, 4, 18, 0, 0, 0))})
        ));

        double total = ordersService.getTotalPaymentsAmountForDate(PaymentType.CASH, LocalDate.of(2024, 4, 18));

        assert total == 0;
    }

    @Test
    @Order(10)
    public void getTotalPaymentsWithDifferentDate() {
        Mockito.when(paymentRepository.getAll()).thenReturn(new ArrayList<>(
                List.of(new Payment[]{new Payment(1, PaymentType.CARD, 100, LocalDateTime.of(2024, 4, 17, 0, 0, 0))})
        ));

        double total = ordersService.getTotalPaymentsAmountForDate(PaymentType.CARD, LocalDate.of(2024, 4, 18));

        assert total == 0;
    }

    @Test
    @Order(11)
    public void getTotalPaymentsWithOneValidPayment() {
        Mockito.when(paymentRepository.getAll()).thenReturn(new ArrayList<>(
                List.of(new Payment[]{new Payment(1, PaymentType.CARD, 100, LocalDateTime.of(2024, 4, 18, 0, 0, 0))})
        ));

        double total = ordersService.getTotalPaymentsAmountForDate(PaymentType.CARD, LocalDate.of(2024, 4, 18));

        assert total == 100;
    }

    @Test
    @Order(12)
    public void getTotalPaymentsWithValidPayments() {
        Mockito.when(paymentRepository.getAll()).thenReturn(new ArrayList<>(
                List.of(new Payment[]{new Payment(1, PaymentType.CARD, 100, LocalDateTime.of(2024, 4, 18, 0, 0, 0)),
                                    new Payment(2, PaymentType.CARD, 150, LocalDateTime.of(2024, 4, 18, 0, 0, 0))})
        ));

        double total = ordersService.getTotalPaymentsAmountForDate(PaymentType.CARD, LocalDate.of(2024, 4, 18));

        assert total == 250;
    }
}