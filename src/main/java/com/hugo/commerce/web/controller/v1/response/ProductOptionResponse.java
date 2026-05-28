package com.hugo.commerce.web.controller.v1.response;

import com.hugo.commerce.domain.model.ProductOption;

import java.math.BigDecimal;

record ProductOptionResponse(
    Long id,
    String name,
    String description,
    BigDecimal salesPrice,
    BigDecimal discountedPrice,
    int stockQuantity
) {

    static ProductOptionResponse from(ProductOption option) {
        return new ProductOptionResponse(
            option.id(),
            option.name(),
            option.description(),
            option.price().salesPrice(),
            option.price().discountedPrice(),
            option.stockQuantity()
        );
    }

}
