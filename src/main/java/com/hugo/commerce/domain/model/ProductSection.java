package com.hugo.commerce.domain.model;

import com.hugo.commerce.domain.enums.ProductSectionType;

public record ProductSection(
    ProductSectionType type,
    String content
) {

}
