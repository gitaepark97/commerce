package com.hugo.commerce.infra.storage.fixture;

import com.hugo.commerce.infra.storage.entity.PriceEmbeddable;

import java.math.BigDecimal;

class FixtureConstants {

    static final PriceEmbeddable DEFAULT_PRICE = new PriceEmbeddable(
        BigDecimal.valueOf(10000), BigDecimal.valueOf(12000), BigDecimal.valueOf(9000)
    );

}
