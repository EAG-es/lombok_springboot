package inser.spring.lombok_springboot.inventory.service;

import inser.spring.lombok_springboot.inventory.dto.InventoryDTO;
import inser.spring.lombok_springboot.inventory.mapper.InventoryMapper;
import inser.spring.lombok_springboot.inventory.model.Inventory;
import inser.spring.lombok_springboot.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import innui.utils.Numbers;
import innui.utils.bundles.Bundles;
import innui.utils.config.NullSafetyConfig;
import innui.utils.sql.Sql;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Implementation of InventoryService.
 * Uses Lombok's @RequiredArgsConstructor for constructor injection
 * and @Slf4j for logging.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

        private final InventoryRepository inventoryRepository;
        private final InventoryMapper inventoryMapper;
        private final Bundles bundle;

        @Override
        @Transactional(readOnly = true)
        public List<InventoryDTO> getAllInventories() {
                log.info(bundle.getMessage("log.fetching.all"));
                return NullSafetyConfig.requireNonNull(inventoryRepository.findAll().stream()
                                .map(i -> inventoryMapper.toDTO(i))
                                .collect(Collectors.toList()));
        }

        @Override
        @Transactional(readOnly = true)
        public Page<InventoryDTO> getSelectedInventories(InventoryDTO inventoryDTO, Pageable pageable) {
                log.info(bundle.getMessage("log.fetching.selected"));
                String where = "";
                if (inventoryDTO.getId() != null && !inventoryDTO.getId().isBlank()) {
                        where = Sql.createAndWhere(where, "id", inventoryDTO.getId(), false);
                }
                if (inventoryDTO.getProductId() != null && !inventoryDTO.getProductId().isBlank()) {
                        where = Sql.createAndWhere(where, "product_id", inventoryDTO.getProductId(), false);
                }
                if (inventoryDTO.getQuantity() != null && !inventoryDTO.getQuantity().isBlank()) {
                        where = Sql.createAndWhere(where, "quantity", inventoryDTO.getQuantity(), false);
                }
                return inventoryRepository.findByWhere(where, pageable)
                                .map(i -> inventoryMapper.toDTO(i));
        }

        @Override
        @Transactional(readOnly = true)
        public InventoryDTO getInventoryById(Long id) {
                log.info(bundle.getMessage("log.fetching.id", id));
                return NullSafetyConfig.requireNonNull(inventoryRepository.findById(id)
                                .map(i -> inventoryMapper.toDTO(i))
                                .orElseThrow(() -> new RuntimeException(bundle.getMessage("inventory.notfound", id))));
        }

        @Override
        @Transactional
        @SuppressWarnings("null")
        public InventoryDTO createInventory(InventoryDTO inventoryDTO) {
                log.info(bundle.getMessage("log.creating", inventoryDTO.getProductId()));

                // Check if inventory for the same Product ID already exists?
                // Or check by ID if provided
                if (inventoryDTO.getId() != null
                                && !inventoryDTO.getId().isBlank()
                                && inventoryRepository.existsById(Numbers.parseLong(inventoryDTO.getId()))) {
                        throw new RuntimeException(bundle.getMessage("inventory.exists", inventoryDTO.getId()));
                }

                Inventory inventory = NullSafetyConfig.requireNonNull(inventoryMapper.toEntity(inventoryDTO));
                // Set ID to null to allow auto-generation if not forced
                inventory.setId(null);
                Inventory savedInventory = inventoryRepository.save(NullSafetyConfig.requireNonNull(inventory));
                return NullSafetyConfig.requireNonNull(inventoryMapper.toDTO(savedInventory));
        }

        @Override
        @Transactional
        @SuppressWarnings("null")
        public InventoryDTO updateInventory(Long id, InventoryDTO inventoryDTO) {
                log.info(bundle.getMessage("log.updating", id));
                Inventory existingInventory = inventoryRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException(bundle.getMessage("inventory.notfound", id)));

                if (inventoryDTO.getProductId() != null && !inventoryDTO.getProductId().isBlank()) {
                        existingInventory.setProductId(Numbers.parseLong(inventoryDTO.getProductId()));
                }
                if (inventoryDTO.getQuantity() != null && !inventoryDTO.getQuantity().isBlank()) {
                        existingInventory.setQuantity(Numbers.parseInt(inventoryDTO.getQuantity()));
                }

                Inventory updatedInventory = inventoryRepository.save(existingInventory);
                return NullSafetyConfig.requireNonNull(inventoryMapper.toDTO(updatedInventory));
        }

        @Override
        @Transactional
        public void deleteInventory(Long id) {
                log.info(bundle.getMessage("log.deleting", id));
                if (!inventoryRepository.existsById(id)) {
                        throw new RuntimeException(bundle.getMessage("inventory.notfound", id));
                }
                inventoryRepository.deleteById(id);
        }
}
