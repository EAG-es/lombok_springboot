package inser.spring.lombok_springboot.order.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    @Test
    public void testOrderLombok() {
        Order order = Order.builder()
                .orderNumber("ORD-123")
                .productId(1L)
                .quantity(2)
                .totalPrice(new BigDecimal("200.00"))
                .build();
        assertEquals("ORD-123", order.getOrderNumber());
        assertEquals(new BigDecimal("200.00"), order.getTotalPrice());
    }
}
