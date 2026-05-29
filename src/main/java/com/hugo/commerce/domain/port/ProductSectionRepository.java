package com.hugo.commerce.domain.port;

import com.hugo.commerce.domain.model.ProductSection;

import java.util.List;

public interface ProductSectionRepository {

    List<ProductSection> findByProductId(Long productId);

}
