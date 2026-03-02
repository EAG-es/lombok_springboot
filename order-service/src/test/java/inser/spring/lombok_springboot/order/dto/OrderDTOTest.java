package inser.spring.lombok_springboot.order.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for OrderDTO to verify Lombok annotations.
 */
public class OrderDTOTest {

    @Test
    public void testOrderDTOLombokAnnotations() {
        OrderDTO dto = OrderDTO.builder()
                .id("1")
                .orderNumber("ORD-123")
                .productId("101")
                .quantity("2")
                .totalPrice("100.00")
                .build();

        assertEquals("1", dto.getId());
        assertEquals("ORD-123", dto.getOrderNumber());
        assertEquals("101", dto.getProductId());
        assertEquals("2", dto.getQuantity());
        assertEquals("100.00", dto.getTotalPrice());

        OrderDTO dto2 = new OrderDTO("1", "ORD-123", "101", "2", "100.00");
        assertEquals(dto, dto2); // Verifies @Data
        assertEquals(dto.hashCode(), dto2.hashCode());
    }
}
