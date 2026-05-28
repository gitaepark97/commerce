package com.hugo.commerce.infra.storage;

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
    public List<Product> findAllById(Collection<Long> ids) {
        return productJpaRepository.findAllByIdInAndStatus(ids, EntityStatus.ACTIVE)
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
