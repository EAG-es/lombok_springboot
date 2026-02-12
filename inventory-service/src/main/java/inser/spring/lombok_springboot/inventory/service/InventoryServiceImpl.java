package inser.spring.lombok_springboot.inventory.service;

import inser.spring.lombok_springboot.inventory.dto.InventoryDTO;
import inser.spring.lombok_springboot.inventory.model.Inventory;
import inser.spring.lombok_springboot.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import inser.spring.lombok_springboot.inventory.config.NullSafetyConfig;

/**
 * Implementation of InventoryService.
 * Uses Lombok's @RequiredArgsConstructor and @Slf4j.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final MessageSource messageSource;

    private String getMessage(String code, Object... args) {
        String message = messageSource.getMessage(code, args, "Message key not found: " + code,
                LocaleContextHolder.getLocale());
        return NullSafetyConfig.requireNonNull(message);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryDTO getStockLevel(Long productId) {
        log.info(getMessage("log.fetching.stock", productId));
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElse(Inventory.builder().productId(productId).quantity(0).build());
        return NullSafetyConfig.requireNonNull(InventoryDTO.builder()
                .productId(NullSafetyConfig.requireNonNull(inventory.getProductId()))
                .quantity(inventory.getQuantity())
                .build());
    }

    @Override
    @Transactional
    public InventoryDTO updateStockLevel(InventoryDTO inventoryDTO) {
        log.info(getMessage("log.updating.stock", inventoryDTO.getProductId()));
        Inventory inventory = inventoryRepository.findByProductId(inventoryDTO.getProductId())
                .orElse(Inventory.builder()
                        .productId(inventoryDTO.getProductId())
                        .quantity(0)
                        .build());

        inventory.setQuantity(inventoryDTO.getQuantity());
        Inventory savedInventory = inventoryRepository.save(inventory);

        return NullSafetyConfig.requireNonNull(InventoryDTO.builder()
                .productId(NullSafetyConfig.requireNonNull(savedInventory.getProductId()))
                .quantity(savedInventory.getQuantity())
                .build());
    }
}
