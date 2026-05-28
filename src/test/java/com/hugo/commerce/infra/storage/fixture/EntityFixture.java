package com.hugo.commerce.infra.storage.fixture;

import com.hugo.commerce.domain.enums.ProductSectionType;
import com.hugo.commerce.domain.enums.ProductStatus;
import com.hugo.commerce.infra.storage.entity.*;

import java.math.BigDecimal;

public class EntityFixture {

    private static final PriceEmbeddable DEFAULT_PRICE = new PriceEmbeddable(
        BigDecimal.valueOf(10000), BigDecimal.valueOf(12000), BigDecimal.valueOf(9000)
    );

    public static ProductEntity activeProduct(Long id) {
        return ProductEntity.builder()
            .id(id)
            .status(EntityStatus.ACTIVE)
            .name("상품 " + id)
            .thumbnailUrl("https://example.com/" + id + ".jpg")
            .description("상품 설명")
            .shortDescription("짧은 설명")
            .price(DEFAULT_PRICE)
            .productStatus(ProductStatus.ACTIVE)
            .build();
    }

    public static ProductEntity inactiveProduct(Long id) {
        return ProductEntity.builder()
            .id(id)
            .status(EntityStatus.ACTIVE)
            .name("상품 " + id)
            .thumbnailUrl("https://example.com/" + id + ".jpg")
            .price(DEFAULT_PRICE)
            .productStatus(ProductStatus.INACTIVE)
            .build();
    }

    public static ProductEntity deletedProduct(Long id) {
        return ProductEntity.builder()
            .id(id)
            .status(EntityStatus.DELETED)
            .name("상품 " + id)
            .thumbnailUrl("https://example.com/" + id + ".jpg")
            .price(DEFAULT_PRICE)
            .productStatus(ProductStatus.INACTIVE)
            .build();
    }

    public static ProductOptionEntity activeOption(Long id, Long productId) {
        return ProductOptionEntity.builder()
            .id(id)
            .status(EntityStatus.ACTIVE)
            .productId(productId)
            .name("옵션 " + id)
            .description("옵션 설명")
            .price(DEFAULT_PRICE)
            .stockQuantity(100)
            .build();
    }

    public static ProductOptionEntity deletedOption(Long id, Long productId) {
        return ProductOptionEntity.builder()
            .id(id)
            .status(EntityStatus.DELETED)
            .productId(productId)
            .name("옵션 " + id)
            .price(DEFAULT_PRICE)
            .stockQuantity(0)
            .build();
    }

    public static ProductSectionEntity activeSection(Long id, Long productId, int sortOrder) {
        return ProductSectionEntity.builder()
            .id(id)
            .status(EntityStatus.ACTIVE)
            .productId(productId)
            .type(ProductSectionType.IMAGE)
            .content("섹션 내용 " + id)
            .sortOrder(sortOrder)
            .build();
    }

    public static ProductSectionEntity deletedSection(Long id, Long productId, int sortOrder) {
        return ProductSectionEntity.builder()
            .id(id)
            .status(EntityStatus.DELETED)
            .productId(productId)
            .type(ProductSectionType.IMAGE)
            .content("섹션 내용 " + id)
            .sortOrder(sortOrder)
            .build();
    }

    public static ProductCategoryEntity activeCategory(Long id, Long productId, Long categoryId) {
        return ProductCategoryEntity.builder()
            .id(id)
            .status(EntityStatus.ACTIVE)
            .productId(productId)
            .categoryId(categoryId)
            .build();
    }

    public static ProductCategoryEntity deletedCategory(Long id, Long productId, Long categoryId) {
        return ProductCategoryEntity.builder()
            .id(id)
            .status(EntityStatus.DELETED)
            .productId(productId)
            .categoryId(categoryId)
            .build();
    }

}
