package com.hugo.commerce.domain.port;

import com.hugo.commerce.domain.model.ProductOption;

import java.util.List;

public interface ProductOptionRepository {

    List<ProductOption> findByProductId(Long productId);

}
