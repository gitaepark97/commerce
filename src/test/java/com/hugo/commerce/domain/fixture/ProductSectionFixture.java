package com.hugo.commerce.domain.fixture;

import com.hugo.commerce.domain.enums.ProductSectionType;
import com.hugo.commerce.domain.model.ProductSection;

public class ProductSectionFixture {

    public static ProductSection create(ProductSectionType type) {
        return new ProductSection(type, "섹션 내용");
    }
}
