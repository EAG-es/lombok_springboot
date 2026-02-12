package inser.spring.lombok_springboot.inventory.controller;

import inser.spring.lombok_springboot.inventory.dto.InventoryDTO;
import inser.spring.lombok_springboot.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Inventory Service.
 */
@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory Controller", description = "Endpoints for managing product stock levels")
public class InventoryController {

    private final InventoryService inventoryService;

    /**
     * Retrieves stock level for a product.
     * 
     * @param productId The ID of the product
     * @return InventoryDTO
     */
    @GetMapping("/{productId}")
    @Operation(summary = "Get stock level", description = "Retrieves the current stock level for a specific product")
    public ResponseEntity<InventoryDTO> getStockLevel(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getStockLevel(productId));
    }

    /**
     * Updates the stock level for a product.
     * 
     * @param inventoryDTO The inventory data to update
     * @return InventoryDTO
     */
    @PostMapping
    @Operation(summary = "Update stock level", description = "Updates the stock level for a product")
    public ResponseEntity<InventoryDTO> updateStockLevel(@RequestBody InventoryDTO inventoryDTO) {
        return ResponseEntity.ok(inventoryService.updateStockLevel(inventoryDTO));
    }
}
