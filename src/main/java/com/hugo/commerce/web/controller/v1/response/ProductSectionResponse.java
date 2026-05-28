package com.hugo.commerce.web.controller.v1.response;

import com.hugo.commerce.domain.enums.ProductSectionType;
import com.hugo.commerce.domain.model.ProductSection;

record ProductSectionResponse(
    ProductSectionType type,
    String content
) {

    static ProductSectionResponse from(ProductSection section) {
        return new ProductSectionResponse(section.type(), section.content());
    }

}
