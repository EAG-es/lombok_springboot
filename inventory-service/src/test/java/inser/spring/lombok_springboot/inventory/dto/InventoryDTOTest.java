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
                .productId(101L)
                .quantity(50)
                .build();

        assertEquals(101L, dto.getProductId());
        assertEquals(50, dto.getQuantity());

        InventoryDTO dto2 = new InventoryDTO();
        dto2.setProductId(101L);
        dto2.setQuantity(50);

        assertEquals(dto, dto2); // Verifies @Data (equals/hashCode)
        assertEquals(dto.hashCode(), dto2.hashCode());
    }
}
