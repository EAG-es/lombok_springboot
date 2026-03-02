package inser.spring.lombok_springboot.order.repository;

import inser.spring.lombok_springboot.order.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<Order> findByWhere(String whereClause, Pageable pageable);
}
