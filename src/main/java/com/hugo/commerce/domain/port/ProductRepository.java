package com.hugo.commerce.domain.port;

import com.hugo.commerce.domain.enums.ProductStatus;
import com.hugo.commerce.domain.model.Product;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> findByIds(Collection<Long> ids);

    List<Product> findByIds(Collection<Long> ids, Collection<ProductStatus> statuses);

    Optional<Product> findById(Long id);

}
