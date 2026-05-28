package com.hugo.commerce.domain.fake;

import com.hugo.commerce.domain.port.ProductCategoryRepository;

import java.util.*;

public class FakeProductCategoryRepository implements ProductCategoryRepository {

    private final Map<Long, List<Long>> store = new HashMap<>();

    public void save(Long categoryId, Long productId) {
        store.computeIfAbsent(categoryId, k -> new ArrayList<>()).add(productId);
    }

    @Override
    public List<Long> findProductIdsByCategoryId(Long categoryId, Long cursor, int limit) {
        List<Long> ids = store.getOrDefault(categoryId, Collections.emptyList());

        int startIndex = 0;
        if (cursor != null) {
            int cursorIndex = ids.indexOf(cursor);
            startIndex = cursorIndex >= 0 ? cursorIndex + 1 : ids.size();
        }

        return new ArrayList<>(ids.subList(startIndex, Math.min(startIndex + limit, ids.size())));
    }
}
