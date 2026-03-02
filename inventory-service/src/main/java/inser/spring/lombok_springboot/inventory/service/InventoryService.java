package inser.spring.lombok_springboot.inventory.service;

import inser.spring.lombok_springboot.inventory.dto.InventoryDTO;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventoryService {
    List<InventoryDTO> getAllInventories();

    Page<InventoryDTO> getSelectedInventories(InventoryDTO inventoryDTO, Pageable pageable);

    InventoryDTO getInventoryById(Long id);

    InventoryDTO createInventory(InventoryDTO inventoryDTO);

    InventoryDTO updateInventory(Long id, InventoryDTO inventoryDTO);

    void deleteInventory(Long id);
}
