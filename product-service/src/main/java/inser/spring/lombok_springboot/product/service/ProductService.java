package inser.spring.lombok_springboot.product.service;

import inser.spring.lombok_springboot.product.dto.ProductDTO;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    List<ProductDTO> getAllProducts();

    Page<ProductDTO> getSelectedProducts(ProductDTO productDTO, Pageable pageable);

    ProductDTO getProductById(Long id);

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO updateProduct(Long id, ProductDTO productDTO);

    void deleteProduct(Long id);
}
