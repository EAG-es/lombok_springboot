package inser.spring.lombok_springboot.order.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for OrderDTO to verify Lombok annotations.
 */
public class OrderDTOTest {

    @Test
    public void testOrderDTOLombokAnnotations() {
        OrderDTO dto = OrderDTO.builder()
                .id(1L)
                .orderNumber("ORD-123")
                .productId(101L)
                .quantity(2)
                .totalPrice(new BigDecimal("100.00"))
                .build();

        assertEquals(1L, dto.getId());
        assertEquals("ORD-123", dto.getOrderNumber());
        assertEquals(101L, dto.getProductId());
        assertEquals(2, dto.getQuantity());
        assertEquals(new BigDecimal("100.00"), dto.getTotalPrice());

        OrderDTO dto2 = new OrderDTO(1L, "ORD-123", 101L, 2, new BigDecimal("100.00"));
        assertEquals(dto, dto2); // Verifies @Data
        assertEquals(dto.hashCode(), dto2.hashCode());
    }
}
