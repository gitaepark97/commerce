package com.hugo.commerce.infra.storage;

import com.hugo.commerce.domain.enums.ProductStatus;
import com.hugo.commerce.domain.model.Product;
import com.hugo.commerce.domain.port.ProductRepository;
import com.hugo.commerce.infra.storage.entity.EntityStatus;
import com.hugo.commerce.infra.storage.entity.ProductEntity;
import com.hugo.commerce.infra.storage.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public List<Product> findByIds(Collection<Long> ids, Collection<ProductStatus> statuses) {
        return productJpaRepository.findAllByIdInAndStatusAndProductStatusIn(ids, EntityStatus.ACTIVE, statuses)
            .stream()
            .map(ProductEntity::toDomain)
            .toList();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
            .map(ProductEntity::toDomain);
    }

}
