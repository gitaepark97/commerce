package com.hugo.commerce.domain.fake;

import com.hugo.commerce.domain.model.ProductOption;
import com.hugo.commerce.domain.port.ProductOptionRepository;

import java.util.*;

public class FakeProductOptionRepository implements ProductOptionRepository {

    private final Map<Long, ProductOption> byId = new HashMap<>();
    private final Map<Long, List<ProductOption>> byProductId = new HashMap<>();

    public void save(Long productId, ProductOption option) {
        byId.put(option.id(), option);
        byProductId.computeIfAbsent(productId, k -> new ArrayList<>()).add(option);
    }

    @Override
    public Optional<ProductOption> findById(Long id) {
        return Optional.ofNullable(byId.get(id));
    }

    @Override
    public List<ProductOption> findByIds(Collection<Long> ids) {
        return ids.stream()
            .map(byId::get)
            .filter(Objects::nonNull)
            .toList();
    }

    @Override
    public List<ProductOption> findByProductId(Long productId) {
        return byProductId.getOrDefault(productId, Collections.emptyList());
    }

}
