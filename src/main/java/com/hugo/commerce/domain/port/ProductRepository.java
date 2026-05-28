package com.hugo.commerce.domain.port;

import com.hugo.commerce.domain.model.Product;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> findAllById(Collection<Long> ids);

    Optional<Product> findById(Long id);

}
