package com.hugo.commerce.domain.application;

import com.hugo.commerce.domain.enums.ProductStatus;
import com.hugo.commerce.domain.model.Product;
import com.hugo.commerce.domain.model.ProductDetail;
import com.hugo.commerce.domain.model.ProductOption;
import com.hugo.commerce.domain.model.ProductOptionDetail;
import com.hugo.commerce.domain.port.ProductCategoryRepository;
import com.hugo.commerce.domain.port.ProductOptionRepository;
import com.hugo.commerce.domain.port.ProductRepository;
import com.hugo.commerce.domain.port.ProductSectionRepository;
import com.hugo.commerce.support.Page;
import com.hugo.commerce.support.PageParam;
import com.hugo.commerce.support.error.CoreException;
import com.hugo.commerce.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
class ProductFinder {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductSectionRepository productSectionRepository;

    Page<Product> findProducts(Long categoryId, PageParam pageParam) {
        // 카테고리-상품 관계 테이블 기준으로 페이지를 잘라야 hasNext가 정확하므로 ID 조회를 분리
        var productIds = productCategoryRepository.findProductIdsByCategoryId(
            categoryId, pageParam.cursor(), pageParam.size() + 1
        );

        boolean hasNext = productIds.size() > pageParam.size();
        List<Long> pagedIds = productIds.stream().limit(pageParam.size()).toList();

        Map<Long, Product> productMap = productRepository.findByIds(pagedIds, ProductStatus.VISIBLE)
            .stream()
            .collect(Collectors.toMap(Product::id, Function.identity()));

        // IN 절은 순서를 보장하지 않으므로 category에서 얻은 productIds 순서로 재정렬
        var products = pagedIds.stream()
            .map(productMap::get)
            .filter(Objects::nonNull)
            .toList();

        return new Page<>(products, hasNext);
    }

    ProductDetail findProduct(Long id) {
        var product = productRepository.findById(id)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND));

        if (product.status() == ProductStatus.INACTIVE) {
            throw new CoreException(ErrorType.PRODUCT_UNAVAILABLE);
        }

        var options = productOptionRepository.findByProductId(id);
        var sections = productSectionRepository.findByProductId(id);

        return new ProductDetail(product, options, sections);
    }

    ProductOption findProductOption(Long optionId) {
        return productOptionRepository.findById(optionId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND));
    }

    List<ProductOptionDetail> findProductOptionDetails(List<Long> optionIds) {
        var options = productOptionRepository.findByIds(optionIds)
            .stream()
            .collect(Collectors.toMap(ProductOption::id, Function.identity()));

        var productIds = options.values().stream()
            .map(ProductOption::productId)
            .collect(Collectors.toSet());
        var products = productRepository.findByIds(productIds).stream()
            .collect(Collectors.toMap(Product::id, Function.identity()));

        return optionIds.stream()
            .filter(options::containsKey)
            .map(options::get)
            .filter(option -> products.containsKey(option.productId()))
            .map(option -> new ProductOptionDetail(products.get(option.productId()), option))
            .toList();
    }

}
