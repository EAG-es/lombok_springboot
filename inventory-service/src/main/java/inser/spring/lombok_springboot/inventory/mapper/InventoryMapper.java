package inser.spring.lombok_springboot.inventory.mapper;

import inser.spring.lombok_springboot.inventory.dto.InventoryDTO;
import inser.spring.lombok_springboot.inventory.model.Inventory;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import innui.utils.Numbers;

/**
 * Mapper for converting between Inventory entity and InventoryDTO.
 */
@Component
public class InventoryMapper {

    @Nullable
    public InventoryDTO toDTO(@Nullable Inventory inventory) {
        if (inventory == null)
            return null;
        return InventoryDTO.builder()
                .id(inventory.getId() != null ? inventory.getId().toString() : null)
                .productId(inventory.getProductId() != null ? inventory.getProductId().toString() : null)
                .quantity(inventory.getQuantity() != null ? inventory.getQuantity().toString() : null)
                .build();
    }

    @Nullable
    public Inventory toEntity(@Nullable InventoryDTO dto) {
        if (dto == null)
            return null;
        return Inventory.builder()
                .id(dto.getId() != null && !dto.getId().isBlank() ? Numbers.parseLong(dto.getId()) : null)
                .productId(dto.getProductId() != null && !dto.getProductId().isBlank()
                        ? Numbers.parseLong(dto.getProductId())
                        : null)
                .quantity(
                        dto.getQuantity() != null && !dto.getQuantity().isBlank() ? Numbers.parseInt(dto.getQuantity())
                                : null)
                .build();
    }
}
