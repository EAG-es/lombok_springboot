package inser.spring.lombok_springboot.inventory.controller;

import inser.spring.lombok_springboot.inventory.dto.InventoryDTO;
import inser.spring.lombok_springboot.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;

import java.util.Locale;

/**
 * REST Controller for Inventory Service.
 * Provides endpoints for managing inventory levels.
 */
@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory Controller", description = "Endpoints for managing inventory in the eCommerce platform")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/select")
    @Operation(summary = "Get selected inventory items", description = "Retrieves a list of selected inventory items")
    public ResponseEntity<PagedModel<InventoryDTO>> getSelectedInventories(
            @RequestBody InventoryDTO inventoryDTO,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize,
            @RequestHeader(value = "X-Location", required = false) String location) {

        // Set locale based on location
        if (location != null && location.contains("lang=es")) {
            LocaleContextHolder.setLocale(new Locale("es"));
        } else {
            LocaleContextHolder.setLocale(new Locale("en"));
        }

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<InventoryDTO> pageResult = inventoryService.getSelectedInventories(inventoryDTO, pageable);
        return ResponseEntity.ok(new PagedModel<>(pageResult));
    }

    /**
     * Retrieves inventory details by ID.
     * 
     * @param id The ID of the inventory record
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get inventory by ID", description = "Retrieves detailed information about an inventory record by its ID")
    public ResponseEntity<InventoryDTO> getInventoryById(@PathVariable Long id,
            @RequestHeader(value = "X-Location", required = false) String location) {

        if (location != null && location.contains("lang=es")) {
            LocaleContextHolder.setLocale(new Locale("es"));
        } else {
            LocaleContextHolder.setLocale(new Locale("en"));
        }

        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }

    /**
     * Adds a new inventory record.
     * 
     * @param inventoryDTO The inventory item to add
     */
    @PostMapping
    @Operation(summary = "Create a new inventory record", description = "Adds a new inventory mapping for a product")
    public ResponseEntity<InventoryDTO> createInventory(@RequestBody InventoryDTO inventoryDTO,
            @RequestHeader(value = "X-Location", required = false) String location) {

        if (location != null && location.contains("lang=es")) {
            LocaleContextHolder.setLocale(new Locale("es"));
        } else {
            LocaleContextHolder.setLocale(new Locale("en"));
        }

        return new ResponseEntity<>(inventoryService.createInventory(inventoryDTO), HttpStatus.CREATED);
    }

    /**
     * Updates an existing inventory record.
     * 
     * @param id           The ID of the inventory record
     * @param inventoryDTO The updated inventory data
     */
    @PutMapping("/{id}")
    public ResponseEntity<InventoryDTO> updateInventory(@PathVariable Long id, @RequestBody InventoryDTO inventoryDTO,
            @RequestHeader(value = "X-Location", required = false) String location) {

        if (location != null && location.contains("lang=es")) {
            LocaleContextHolder.setLocale(new Locale("es"));
        } else {
            LocaleContextHolder.setLocale(new Locale("en"));
        }

        return ResponseEntity.ok(inventoryService.updateInventory(id, inventoryDTO));
    }

    /**
     * Deletes an inventory record by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id,
            @RequestHeader(value = "X-Location", required = false) String location) {

        if (location != null && location.contains("lang=es")) {
            LocaleContextHolder.setLocale(new Locale("es"));
        } else {
            LocaleContextHolder.setLocale(new Locale("en"));
        }

        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }
}
