package inser.spring.lombok_springboot.inventory.repository;

import inser.spring.lombok_springboot.inventory.model.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventoryRepositoryCustom {
    Page<Inventory> findByWhere(String whereClause, Pageable pageable);
}
