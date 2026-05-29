package com.hugo.commerce.domain.model;

public record ProductOption(
    Long id,
    Long productId,
    String name,
    String description,
    Price price,
    int stockQuantity
) {

}
