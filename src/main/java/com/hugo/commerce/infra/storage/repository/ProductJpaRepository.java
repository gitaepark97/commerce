package com.hugo.commerce.infra.storage.repository;

import com.hugo.commerce.infra.storage.entity.EntityStatus;
import com.hugo.commerce.infra.storage.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findAllByIdInAndStatus(Collection<Long> ids, EntityStatus status);

    Optional<ProductEntity> findByIdAndStatus(Long id, EntityStatus status);

}
