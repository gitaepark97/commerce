package com.hugo.commerce.domain.fake;

import com.hugo.commerce.domain.model.ProductSection;
import com.hugo.commerce.domain.port.ProductSectionRepository;

import java.util.*;

public class FakeProductSectionRepository implements ProductSectionRepository {

    private final Map<Long, List<ProductSection>> store = new HashMap<>();

    public void save(Long productId, ProductSection section) {
        store.computeIfAbsent(productId, k -> new ArrayList<>()).add(section);
    }

    @Override
    public List<ProductSection> findByProductId(Long productId) {
        return store.getOrDefault(productId, Collections.emptyList());
    }
}
