package com.hugo.commerce.domain.model;

import java.util.List;

public record ProductDetail(
    Product product,
    List<ProductOption> options,
    List<ProductSection> sections
) {

}
