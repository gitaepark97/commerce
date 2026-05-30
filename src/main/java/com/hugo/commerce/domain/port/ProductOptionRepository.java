package com.hugo.commerce.domain.port;

import com.hugo.commerce.domain.model.ProductOption;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductOptionRepository {

    Optional<ProductOption> findById(Long id);

    List<ProductOption> findByProductId(Long productId);

    List<ProductOption> findByIds(Collection<Long> ids);

}
