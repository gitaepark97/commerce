package com.hugo.commerce.domain.application;

import com.hugo.commerce.domain.model.Product;
import com.hugo.commerce.domain.model.ProductDetail;
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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class ProductFinder {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductSectionRepository productSectionRepository;

    Page<Product> findProductsByCategoryId(Long categoryId, PageParam pageParam) {
        // 카테고리-상품 관계 테이블 기준으로 페이지를 잘라야 hasNext가 정확하므로 ID 조회를 분리
        // COUNT 쿼리 없이 다음 페이지 존재 여부를 판단하기 위해 1개 더 조회
        var productIds = productCategoryRepository.findProductIdsByCategoryId(
            categoryId, pageParam.cursor(), pageParam.size() + 1
        );

        boolean hasNext = productIds.size() > pageParam.size();
        List<Long> pagedIds = productIds.stream().limit(pageParam.size()).toList();

        Map<Long, Product> productMap = productRepository.findAllById(pagedIds)
            .stream()
            .collect(Collectors.toMap(Product::id, Function.identity()));

        // IN 절은 순서를 보장하지 않으므로 category에서 얻은 productIds 순서로 재정렬
        var products = pagedIds.stream()
            .map(productMap::get)
            .filter(Objects::nonNull)
            .toList();

        return new Page<>(products, hasNext);
    }

    ProductDetail findProductById(Long id) {
        var product = productRepository.findById(id)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND));

        var options = productOptionRepository.findByProductId(id);
        var sections = productSectionRepository.findByProductId(id);

        return new ProductDetail(product, options, sections);
    }

}
