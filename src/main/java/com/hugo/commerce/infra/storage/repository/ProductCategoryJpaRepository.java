package com.hugo.commerce.infra.storage.repository;

import com.hugo.commerce.infra.storage.entity.EntityStatus;
import com.hugo.commerce.infra.storage.entity.ProductCategoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductCategoryJpaRepository extends JpaRepository<ProductCategoryEntity, Long> {

    @Query("SELECT pc.productId FROM ProductCategoryEntity pc WHERE pc.categoryId = :categoryId AND pc.status = :status")
    List<Long> findProductIdsByCategoryIdAndStatus(@Param("categoryId") Long categoryId, @Param("status") EntityStatus status, Pageable pageable);

    @Query("SELECT pc.productId FROM ProductCategoryEntity pc WHERE pc.categoryId = :categoryId AND pc.status = :status AND pc.productId > :cursor")
    List<Long> findProductIdsByCategoryIdAndStatusAndProductIdGreaterThan(@Param("categoryId") Long categoryId, @Param("status") EntityStatus status, @Param("cursor") Long cursor, Pageable pageable);

}
