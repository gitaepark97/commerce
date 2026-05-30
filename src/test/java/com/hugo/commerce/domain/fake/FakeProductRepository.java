package com.hugo.commerce.domain.fake;

import com.hugo.commerce.domain.enums.ProductStatus;
import com.hugo.commerce.domain.model.Product;
import com.hugo.commerce.domain.port.ProductRepository;

import java.util.*;

public class FakeProductRepository implements ProductRepository {

    private final Map<Long, Product> store = new HashMap<>();

    public void save(Product product) {
        store.put(product.id(), product);
    }

    @Override
    public List<Product> findByIds(Collection<Long> ids) {
        return ids.stream()
            .map(store::get)
            .filter(Objects::nonNull)
            .toList();
    }

    @Override
    public List<Product> findByIds(Collection<Long> ids, Collection<ProductStatus> statuses) {
        return ids.stream()
            .map(store::get)
            .filter(Objects::nonNull)
            .filter(p -> statuses.contains(p.status()))
            .toList();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }
}
