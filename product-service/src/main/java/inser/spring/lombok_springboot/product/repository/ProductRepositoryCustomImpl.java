package inser.spring.lombok_springboot.product.repository;

import inser.spring.lombok_springboot.product.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Product> findByWhere(String whereClause, Pageable pageable) {
        String sql = "SELECT * FROM products p";
        String countSql = "SELECT COUNT(*) FROM products p";

        if (whereClause != null && !whereClause.isBlank()) {
            sql += " WHERE " + whereClause;
            countSql += " WHERE " + whereClause;
        }

        int offset = (int) pageable.getOffset();
        int limit = pageable.getPageSize();
        sql += " OFFSET " + offset + " ROWS FETCH NEXT " + limit + " ROWS ONLY";

        Query query = entityManager.createNativeQuery(sql, Product.class);
        Query countQuery = entityManager.createNativeQuery(countSql);

        List<Product> result = query.getResultList();
        Long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<Product>(result, pageable, total);
    }
}
