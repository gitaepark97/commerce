package com.hugo.commerce.domain.fixture;

import com.hugo.commerce.domain.enums.ProductStatus;
import com.hugo.commerce.domain.model.Price;
import com.hugo.commerce.domain.model.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product create(Long id) {
        return new Product(
            id,
            "상품 " + id,
            "https://example.com/thumbnail/" + id + ".jpg",
            "상품 설명",
            "짧은 설명",
            new Price(
                BigDecimal.valueOf(10000),
                BigDecimal.valueOf(12000),
                BigDecimal.valueOf(9000)
            ),
            ProductStatus.ACTIVE
        );
    }

    public static Product inactive(Long id) {
        return new Product(
            id,
            "상품 " + id,
            "https://example.com/thumbnail/" + id + ".jpg",
            null,
            null,
            new Price(
                BigDecimal.valueOf(10000),
                BigDecimal.valueOf(12000),
                BigDecimal.valueOf(9000)
            ),
            ProductStatus.INACTIVE
        );
    }
}
