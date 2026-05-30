package com.hugo.commerce.infra.storage.repository;

import com.hugo.commerce.infra.storage.entity.CartItemEntity;
import com.hugo.commerce.infra.storage.entity.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemJpaRepository extends JpaRepository<CartItemEntity, Long> {

    Optional<CartItemEntity> findByIdAndUserIdAndStatus(Long id, Long userId, EntityStatus status);

    Optional<CartItemEntity> findByUserIdAndProductOptionIdAndStatus(Long userId, Long productOptionId, EntityStatus status);

    List<CartItemEntity> findByUserIdAndStatus(Long userId, EntityStatus status);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE CartItemEntity c SET c.status = :status WHERE c.id = :id AND c.userId = :userId AND c.status = 'ACTIVE'")
    int updateStatusByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId, @Param("status") EntityStatus status);

}
