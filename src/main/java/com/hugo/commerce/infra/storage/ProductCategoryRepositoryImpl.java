package com.hugo.commerce.infra.storage;

import com.hugo.commerce.domain.port.ProductCategoryRepository;
import com.hugo.commerce.infra.storage.entity.EntityStatus;
import com.hugo.commerce.infra.storage.repository.ProductCategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductCategoryRepositoryImpl implements ProductCategoryRepository {

    private static final Sort PRODUCT_ID_ASC = Sort.by("productId").ascending();

    private final ProductCategoryJpaRepository productCategoryJpaRepository;

    @Override
    public List<Long> findProductIdsByCategoryId(Long categoryId, Long cursor, int limit) {
        PageRequest pageable = PageRequest.of(0, limit, PRODUCT_ID_ASC);

        return cursor == null
            ? productCategoryJpaRepository.findProductIdsByCategoryIdAndStatus(categoryId, EntityStatus.ACTIVE, pageable)
            : productCategoryJpaRepository.findProductIdsByCategoryIdAndStatusAndProductIdGreaterThan(categoryId, EntityStatus.ACTIVE, cursor, pageable);
    }

}
