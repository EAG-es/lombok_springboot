package inser.spring.lombok_springboot.product.service;

import inser.spring.lombok_springboot.product.dto.ProductDTO;
import java.util.List;

/**
 * Service interface for managing Products.
 */
public interface ProductService {
    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(Long id);

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO updateProduct(Long id, ProductDTO productDTO);

    void deleteProduct(Long id);
}
