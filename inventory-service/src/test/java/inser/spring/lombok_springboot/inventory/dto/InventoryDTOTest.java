package inser.spring.lombok_springboot.inventory.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for InventoryDTO to verify Lombok annotations.
 */
public class InventoryDTOTest {

    @Test
    public void testInventoryDTOLombokAnnotations() {
        InventoryDTO dto = InventoryDTO.builder()
                .id("1")
                .productId("101")
                .quantity("50")
                .build();

        assertEquals("1", dto.getId());
        assertEquals("101", dto.getProductId());
        assertEquals("50", dto.getQuantity());

        InventoryDTO dto2 = new InventoryDTO();
        dto2.setId("1");
        dto2.setProductId("101");
        dto2.setQuantity("50");

        assertEquals(dto, dto2); // Verifies @Data (equals/hashCode)
        assertEquals(dto.hashCode(), dto2.hashCode());
    }
}
