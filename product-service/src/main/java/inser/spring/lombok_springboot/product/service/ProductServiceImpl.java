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
import inser.spring.lombok_springboot.product.config.NullSafetyConfig;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
    private final MessageSource messageSource;

    private String getMessage(String code, Object... args) {
        return NullSafetyConfig.requireNonNull(
                messageSource.getMessage(code, args, "Message key not found: " + code,
                        LocaleContextHolder.getLocale()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        log.info(getMessage("log.fetching.all"));
        return NullSafetyConfig.requireNonNull(productRepository.findAll().stream()
                .map(p -> productMapper.toDTO(p))
                .collect(Collectors.toList()));
    }

    /**
     * Guards against SQL injection by rejecting values that contain dangerous
     * SQL keywords, comment sequences, statement terminators, or quote characters.
     *
     * <p>
     * This is a defence-in-depth safeguard. The primary protection should always
     * be parameterised queries / JPA criteria; use this when raw string fragments
     * must be concatenated into a WHERE clause.
     * </p>
     *
     * @param value the user-supplied string to validate
     * @return the original value, trimmed, if it is considered safe
     * @throws IllegalArgumentException if the value looks like a SQL injection
     *                                  attempt
     */
    private String sanitizeSqlValue(String value) {
        String trimmed = value.trim();

        // Reject empty strings – they have no semantic value in a WHERE clause
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(getMessage("error.sql.empty.value"));
        }

        // Reject single/double quotes – they can escape string delimiters
        if (trimmed.contains("'") || trimmed.contains("\"")) {
            throw new IllegalArgumentException(
                    getMessage("error.sql.injection", value));
        }

        // Reject SQL comment sequences
        if (trimmed.contains("--") || trimmed.contains("/*") || trimmed.contains("*/")) {
            throw new IllegalArgumentException(
                    getMessage("error.sql.injection", value));
        }

        // Reject semicolons – they allow stacking multiple statements
        if (trimmed.contains(";")) {
            throw new IllegalArgumentException(
                    getMessage("error.sql.injection", value));
        }

        // Reject dangerous SQL keywords (case-insensitive, word-boundary aware)
        Pattern sqlKeywords = Pattern.compile(
                "(?i)\\b(SELECT|INSERT|UPDATE|DELETE|DROP|CREATE|ALTER|TRUNCATE"
                        + "|EXEC|EXECUTE|UNION|MERGE|CALL|GRANT|REVOKE|LOAD|OUTFILE"
                        + "|INTO|FROM|WHERE|OR|AND|HAVING|GROUP|ORDER|LIMIT)\\b");
        if (sqlKeywords.matcher(trimmed).find()) {
            throw new IllegalArgumentException(
                    getMessage("error.sql.injection", value));
        }

        return trimmed;
    }

    public String createAndWhere(String where, String field, String data, boolean isString) {
        int pos = 0;
        boolean is_there_condition_one = false;
        String prefix = " ", sufix = "";
        String operator;
        String data_two = "";
        data = data.trim();
        data = sanitizeSqlValue(data);
        if (isString) {
            prefix = "'";
            sufix = "'";
        }
        pos = data.indexOf("\\,");
        if (pos > 0) {
            is_there_condition_one = true;
            data_two = data.substring(pos + 2).trim();
            data = data.substring(0, pos).trim();
        }
        if (where.isBlank() == false) {
            where = where + " AND ";
        }
        if (data.startsWith("<=") || data.startsWith(">=")) {
            operator = " " + data.substring(0, 2) + " ";
            where = where + field + operator + prefix + data.substring(2).trim() + sufix;
        } else if (data.startsWith("<") || data.startsWith(">") || data.startsWith("=")) {
            operator = " " + data.substring(0, 1) + " ";
            where = where + field + operator + prefix + data.substring(1).trim() + sufix;
        }
        if (is_there_condition_one) {
            pos = data_two.indexOf("<=");
            if (pos >= 0) {
                operator = " " + data_two.substring(pos, pos + 2) + " ";
                where = where + " AND " + field + operator + prefix + data_two.substring(pos + 2).trim() + sufix;
            } else {
                pos = data_two.indexOf(">=");
                if (pos >= 0) {
                    operator = " " + data_two.substring(pos, pos + 2) + " ";
                    where = where + " AND " + field + operator + prefix + data_two.substring(pos + 2).trim() + sufix;
                } else {
                    pos = data_two.indexOf("<");
                    if (pos >= 0) {
                        operator = " " + data_two.substring(pos, pos + 1) + " ";
                        where = where + " AND " + field + operator + prefix + data_two.substring(pos + 1).trim()
                                + sufix;
                    } else {
                        pos = data_two.indexOf(">");
                        if (pos >= 0) {
                            operator = " " + data_two.substring(pos, pos + 1) + " ";
                            where = where + " AND " + field + operator + prefix + data_two.substring(pos + 1).trim()
                                    + sufix;
                        } else {
                            pos = data_two.indexOf("=");
                            if (pos >= 0) {
                                operator = " " + data_two.substring(pos, pos + 1) + " ";
                                where = where + " AND " + field + operator + prefix + data_two.substring(pos + 1).trim()
                                        + sufix;
                            }
                        }
                    }
                }
            }
        } else {
            where = where + field + " = " + prefix + data + sufix;
        }
        where = where.replace("\\_", " ");
        return where;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getSelectedProducts(ProductDTO productDTO, Pageable pageable) {
        log.info(getMessage("log.fetching.selected"));
        String where = "";
        if (productDTO.getId() != null && productDTO.getId().isEmpty() == false) {
            where = createAndWhere(where, "id", productDTO.getId(), false);
        }
        if (productDTO.getName() != null && productDTO.getName().isEmpty() == false) {
            where = createAndWhere(where, "name", productDTO.getName(), true);
        }
        if (productDTO.getDescription() != null && productDTO.getDescription().isEmpty() == false) {
            where = createAndWhere(where, "description", productDTO.getDescription(), true);
        }
        if (productDTO.getPrice() != null && productDTO.getPrice().isEmpty() == false) {
            where = createAndWhere(where, "price", productDTO.getPrice(), false);
        }
        return productRepository.findByWhere(where,
                pageable)
                .map(p -> productMapper.toDTO(p));
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

        // Check if product with the same ID already exists
        if (productDTO.getId() != null
                && productDTO.getId().isEmpty() == false
                && productRepository.existsById(Numbers.parseLong(productDTO.getId()))) {
            throw new RuntimeException(getMessage("product.exists", productDTO.getId()));
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
        log.info(getMessage("log.updating", id));
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(getMessage("product.notfound", id)));

        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(BigDecimal.valueOf(Numbers.parseDouble(productDTO.getPrice())));

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
