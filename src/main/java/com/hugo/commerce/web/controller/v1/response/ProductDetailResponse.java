package com.hugo.commerce.web.controller.v1.response;

import com.hugo.commerce.domain.enums.ProductStatus;
import com.hugo.commerce.domain.model.ProductDetail;

import java.math.BigDecimal;
import java.util.List;

public record ProductDetailResponse(
    Long id,
    String name,
    String thumbnailUrl,
    String description,
    String shortDescription,
    BigDecimal salesPrice,
    BigDecimal discountedPrice,
    ProductStatus status,
    List<ProductOptionResponse> options,
    List<ProductSectionResponse> sections
) {

    public static ProductDetailResponse from(ProductDetail detail) {
        var product = detail.product();
        var price = product.price();
        return new ProductDetailResponse(
            product.id(),
            product.name(),
            product.thumbnailUrl(),
            product.description(),
            product.shortDescription(),
            price.salesPrice(),
            price.discountedPrice(),
            product.status(),
            detail.options().stream().map(ProductOptionResponse::from).toList(),
            detail.sections().stream().map(ProductSectionResponse::from).toList()
        );
    }

}
