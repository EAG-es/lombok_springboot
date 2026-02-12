package inser.spring.lombok_springboot.inventory.service;

import inser.spring.lombok_springboot.inventory.dto.InventoryDTO;

/**
 * Service interface for managing Inventory.
 */
public interface InventoryService {
    InventoryDTO getStockLevel(Long productId);

    InventoryDTO updateStockLevel(InventoryDTO inventoryDTO);
}
