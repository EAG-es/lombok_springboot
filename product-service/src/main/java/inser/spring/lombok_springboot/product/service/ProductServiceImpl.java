package inser.spring.lombok_springboot.product.service;

import inser.spring.lombok_springboot.product.dto.ProductDTO;
import inser.spring.lombok_springboot.product.mapper.ProductMapper;
import inser.spring.lombok_springboot.product.model.Product;
import inser.spring.lombok_springboot.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import innui.utils.Numbers;
import innui.utils.bundles.Bundles;
import innui.utils.config.NullSafetyConfig;
import innui.utils.sql.Sql;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Implementation of ProductService.
 * Uses Lombok's @RequiredArgsConstructor for constructor injection
 * and @Slf4j for logging.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final Bundles bundle;

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        log.info(bundle.getMessage("log.fetching.all"));
        return NullSafetyConfig.requireNonNull(productRepository.findAll().stream()
                .map(p -> productMapper.toDTO(p))
                .collect(Collectors.toList()));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getSelectedProducts(ProductDTO productDTO, Pageable pageable) {
        log.info(bundle.getMessage("log.fetching.selected"));
        String where = "";
        if (productDTO.getId() != null && productDTO.getId().isEmpty() == false) {
            where = Sql.createAndWhere(where, "id", productDTO.getId(), false);
        }
        if (productDTO.getName() != null && productDTO.getName().isEmpty() == false) {
            where = Sql.createAndWhere(where, "name", productDTO.getName(), true);
        }
        if (productDTO.getDescription() != null && productDTO.getDescription().isEmpty() == false) {
            where = Sql.createAndWhere(where, "description", productDTO.getDescription(), true);
        }
        if (productDTO.getPrice() != null && productDTO.getPrice().isEmpty() == false) {
            where = Sql.createAndWhere(where, "price", productDTO.getPrice(), false);
        }
        return productRepository.findByWhere(where,
                pageable)
                .map(p -> productMapper.toDTO(p));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        log.info(bundle.getMessage("log.fetching.id", id));
        return NullSafetyConfig.requireNonNull(productRepository.findById(id)
                .map(p -> productMapper.toDTO(p))
                .orElseThrow(() -> new RuntimeException(bundle.getMessage("product.notfound", id))));
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        log.info(bundle.getMessage("log.creating", productDTO.getName()));

        // Check if product with the same ID already exists
        if (productDTO.getId() != null
                && productDTO.getId().isEmpty() == false
                && productRepository.existsById(Numbers.parseLong(productDTO.getId()))) {
            throw new RuntimeException(bundle.getMessage("product.exists", productDTO.getId()));
        }

        Product product = NullSafetyConfig.requireNonNull(productMapper.toEntity(productDTO));
        // Set ID to null to allow auto-generation
        product.setId(null);
        Product savedProduct = productRepository.save(NullSafetyConfig.requireNonNull(product));
        return NullSafetyConfig.requireNonNull(productMapper.toDTO(savedProduct));
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        log.info(bundle.getMessage("log.updating", id));
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(bundle.getMessage("product.notfound", id)));

        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(BigDecimal.valueOf(Numbers.parseDouble(productDTO.getPrice())));

        Product updatedProduct = productRepository.save(existingProduct);
        return NullSafetyConfig.requireNonNull(productMapper.toDTO(updatedProduct));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        log.info(bundle.getMessage("log.deleting", id));
        if (!productRepository.existsById(id)) {
            throw new RuntimeException(bundle.getMessage("product.notfound", id));
        }
        productRepository.deleteById(id);
    }
}
