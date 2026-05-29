package com.hugo.commerce.web.controller.v1.response;

import com.hugo.commerce.domain.enums.ProductStatus;
import com.hugo.commerce.domain.model.Product;

import java.math.BigDecimal;

public record ProductResponse(
    Long id,
    String name,
    String thumbnailUrl,
    String shortDescription,
    BigDecimal salesPrice,
    BigDecimal discountedPrice,
    ProductStatus status
) {

    public static ProductResponse from(Product product) {
        var price = product.price();
        return new ProductResponse(
            product.id(),
            product.name(),
            product.thumbnailUrl(),
            product.shortDescription(),
            price.salesPrice(),
            price.discountedPrice(),
            product.status()
        );
    }

}
