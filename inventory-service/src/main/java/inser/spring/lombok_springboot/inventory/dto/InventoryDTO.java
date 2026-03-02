package inser.spring.lombok_springboot.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Inventory.
 * Facilitates communication between layers and API clients.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryDTO {

    private String id;
    private String productId;
    private String quantity;
}
