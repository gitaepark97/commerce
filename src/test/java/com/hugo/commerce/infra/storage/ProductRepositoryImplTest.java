package com.hugo.commerce.infra.storage;

import com.hugo.commerce.domain.enums.ProductStatus;
import com.hugo.commerce.domain.model.Product;
import com.hugo.commerce.domain.port.ProductRepository;
import com.hugo.commerce.infra.storage.fixture.ProductEntityFixture;
import com.hugo.commerce.infra.storage.repository.ProductJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class ProductRepositoryImplTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Test
    @DisplayName("여러 ID로 ACTIVE 상품 목록 반환")
    void findByIds_returnsActiveProducts() {
        // given
        productJpaRepository.save(ProductEntityFixture.active(1L));
        productJpaRepository.save(ProductEntityFixture.active(2L));

        // when
        List<Product> result = productRepository.findByIds(List.of(1L, 2L), ProductStatus.VISIBLE);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Product::id).containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    @DisplayName("DELETED 상태 상품은 목록 조회에서 제외")
    void findByIds_excludesDeletedProducts() {
        // given
        productJpaRepository.save(ProductEntityFixture.active(1L));
        productJpaRepository.save(ProductEntityFixture.deleted(2L));

        // when
        List<Product> result = productRepository.findByIds(List.of(1L, 2L), ProductStatus.VISIBLE);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("ProductStatus.INACTIVE 상품은 목록 조회에서 제외")
    void findByIds_excludesInactiveProducts() {
        // given
        productJpaRepository.save(ProductEntityFixture.active(1L));
        productJpaRepository.save(ProductEntityFixture.inactive(2L));

        // when
        List<Product> result = productRepository.findByIds(List.of(1L, 2L), ProductStatus.VISIBLE);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("존재하지 않는 ID는 목록 조회 결과에 포함되지 않음")
    void findByIds_ignoresMissingIds() {
        // given
        productJpaRepository.save(ProductEntityFixture.active(1L));

        // when
        List<Product> result = productRepository.findByIds(List.of(1L, 999L), ProductStatus.VISIBLE);

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("ID로 ACTIVE 상품 단건 조회")
    void findById_returnsActiveProduct() {
        // given
        productJpaRepository.save(ProductEntityFixture.active(1L));

        // when
        Optional<Product> result = productRepository.findById(1L);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("DELETED 상태 상품은 단건 조회에서 제외")
    void findById_excludesDeletedProduct() {
        // given
        productJpaRepository.save(ProductEntityFixture.deleted(1L));

        // when
        Optional<Product> result = productRepository.findById(1L);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회하면 empty 반환")
    void findById_returnsEmpty_whenNotFound() {
        // when
        Optional<Product> result = productRepository.findById(999L);

        // then
        assertThat(result).isEmpty();
    }

}
