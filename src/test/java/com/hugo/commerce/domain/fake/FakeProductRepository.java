package com.hugo.commerce.domain.fake;

import com.hugo.commerce.domain.model.Product;
import com.hugo.commerce.domain.port.ProductRepository;

import java.util.*;

public class FakeProductRepository implements ProductRepository {

    private final Map<Long, Product> store = new HashMap<>();

    public void save(Product product) {
        store.put(product.id(), product);
    }

    @Override
    public List<Product> findAllById(Collection<Long> ids) {
        return ids.stream()
            .map(store::get)
            .filter(Objects::nonNull)
            .toList();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }
}
