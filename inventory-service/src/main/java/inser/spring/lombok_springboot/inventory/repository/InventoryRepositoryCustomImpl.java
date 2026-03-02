package inser.spring.lombok_springboot.inventory.repository;

import inser.spring.lombok_springboot.inventory.model.Inventory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import innui.utils.config.NullSafetyConfig;

@Repository
public class InventoryRepositoryCustomImpl implements InventoryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Inventory> findByWhere(String whereClause, Pageable pageable) {
        String sql = "SELECT * FROM inventory i";
        String countSql = "SELECT COUNT(*) FROM inventory i";

        if (whereClause != null && !whereClause.isBlank()) {
            sql += " WHERE " + whereClause;
            countSql += " WHERE " + whereClause;
        }

        int offset = (int) pageable.getOffset();
        int limit = pageable.getPageSize();
        sql += " OFFSET " + offset + " ROWS FETCH NEXT " + limit + " ROWS ONLY";

        Query query = entityManager.createNativeQuery(sql, Inventory.class);
        Query countQuery = entityManager.createNativeQuery(countSql);

        @SuppressWarnings("unchecked")
        List<Inventory> result = query.getResultList();
        Long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<Inventory>(NullSafetyConfig.requireNonNull(result), pageable, total);
    }
}
