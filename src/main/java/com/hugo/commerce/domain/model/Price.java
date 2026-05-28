package com.hugo.commerce.domain.model;

import java.math.BigDecimal;

public record Price(
    BigDecimal costPrice,
    BigDecimal salesPrice,
    BigDecimal discountedPrice
) {

}
