package inser.spring.lombok_springboot.order.repository;

import inser.spring.lombok_springboot.order.model.Order;
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
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Order> findByWhere(String whereClause, Pageable pageable) {
        String sql = "SELECT * FROM orders i";
        String countSql = "SELECT COUNT(*) FROM orders i";

        if (whereClause != null && !whereClause.isBlank()) {
            sql += " WHERE " + whereClause;
            countSql += " WHERE " + whereClause;
        }

        int offset = (int) pageable.getOffset();
        int limit = pageable.getPageSize();
        sql += " OFFSET " + offset + " ROWS FETCH NEXT " + limit + " ROWS ONLY";

        Query query = entityManager.createNativeQuery(sql, Order.class);
        Query countQuery = entityManager.createNativeQuery(countSql);

        @SuppressWarnings("unchecked")
        List<Order> result = query.getResultList();
        Long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<Order>(NullSafetyConfig.requireNonNull(result), pageable, total);
    }
}
