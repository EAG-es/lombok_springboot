package inser.spring.lombok_springboot.product.repository;

import inser.spring.lombok_springboot.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<Product> findByWhere(String whereClause, Pageable pageable);
}
