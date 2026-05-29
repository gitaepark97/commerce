package com.hugo.commerce.domain.model;

import com.hugo.commerce.domain.enums.ProductStatus;

public record Product(
    Long id,
    String name,
    String thumbnailUrl,
    String description,
    String shortDescription,
    Price price,
    ProductStatus status
) {

}
