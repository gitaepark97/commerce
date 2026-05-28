package com.hugo.commerce.domain.fixture;

import com.hugo.commerce.domain.model.Price;
import com.hugo.commerce.domain.model.ProductOption;

import java.math.BigDecimal;

public class ProductOptionFixture {

    public static ProductOption create(Long id, Long productId) {
        return new ProductOption(
            id,
            productId,
            "옵션 " + id,
            "옵션 설명",
            new Price(
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(1200),
                BigDecimal.valueOf(900)
            ),
            100
        );
    }
}
