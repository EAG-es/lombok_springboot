package inser.spring.lombok_springboot.inventory.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    @Test
    public void testInventoryLombok() {
        Inventory inventory = Inventory.builder()
                .productId(101L)
                .quantity(50)
                .build();
        assertEquals(101L, inventory.getProductId());
        assertEquals(50, inventory.getQuantity());
    }
}
