package inser.spring.lombok_springboot.product.service;

import inser.spring.lombok_springboot.product.dto.ProductDTO;
import inser.spring.lombok_springboot.product.mapper.ProductMapper;
import inser.spring.lombok_springboot.product.model.Product;
import inser.spring.lombok_springboot.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import inser.spring.lombok_springboot.product.config.NullSafetyConfig;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

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
    private final MessageSource messageSource;

    private String getMessage(String code, Object... args) {
        String message = messageSource.getMessage(code, args, "Message key not found: " + code,
                LocaleContextHolder.getLocale());
        return NullSafetyConfig.requireNonNull(message);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        log.info(getMessage("log.fetching.all"));
        return NullSafetyConfig.requireNonNull(productRepository.findAll().stream()
                .map(p -> productMapper.toDTO(p))
                .collect(Collectors.toList()));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        log.info(getMessage("log.fetching.id", id));
        return NullSafetyConfig.requireNonNull(productRepository.findById(id)
                .map(p -> productMapper.toDTO(p))
                .orElseThrow(() -> new RuntimeException(getMessage("product.notfound", id))));
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        log.info(getMessage("log.creating", productDTO.getName()));
        Product product = productMapper.toEntity(productDTO);
        Product savedProduct = productRepository.save(NullSafetyConfig.requireNonNull(product));
        return NullSafetyConfig.requireNonNull(productMapper.toDTO(savedProduct));
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        log.info(getMessage("log.updating", id));
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(getMessage("product.notfound", id)));

        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());

        Product updatedProduct = productRepository.save(existingProduct);
        return NullSafetyConfig.requireNonNull(productMapper.toDTO(updatedProduct));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        log.info(getMessage("log.deleting", id));
        if (!productRepository.existsById(id)) {
            throw new RuntimeException(getMessage("product.notfound", id));
        }
        productRepository.deleteById(id);
    }
}
