package com.hugo.commerce.domain.fake;

import com.hugo.commerce.domain.model.ProductOption;
import com.hugo.commerce.domain.port.ProductOptionRepository;

import java.util.*;

public class FakeProductOptionRepository implements ProductOptionRepository {

    private final Map<Long, List<ProductOption>> store = new HashMap<>();

    public void save(Long productId, ProductOption option) {
        store.computeIfAbsent(productId, k -> new ArrayList<>()).add(option);
    }

    @Override
    public List<ProductOption> findByProductId(Long productId) {
        return store.getOrDefault(productId, Collections.emptyList());
    }
}
