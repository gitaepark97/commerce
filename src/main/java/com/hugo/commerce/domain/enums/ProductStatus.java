package com.hugo.commerce.domain.enums;

import java.util.Set;

public enum ProductStatus {
    ACTIVE,
    SOLD_OUT,
    INACTIVE;

    public static final Set<ProductStatus> VISIBLE = Set.of(ACTIVE, SOLD_OUT);
}
