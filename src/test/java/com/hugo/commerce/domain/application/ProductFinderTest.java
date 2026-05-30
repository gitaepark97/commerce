package com.hugo.commerce.domain.application;

import com.hugo.commerce.domain.enums.ProductSectionType;
import com.hugo.commerce.domain.fake.FakeProductCategoryRepository;
import com.hugo.commerce.domain.fake.FakeProductOptionRepository;
import com.hugo.commerce.domain.fake.FakeProductRepository;
import com.hugo.commerce.domain.fake.FakeProductSectionRepository;
import com.hugo.commerce.domain.fixture.ProductFixture;
import com.hugo.commerce.domain.fixture.ProductOptionFixture;
import com.hugo.commerce.domain.fixture.ProductSectionFixture;
import com.hugo.commerce.domain.model.Product;
import com.hugo.commerce.domain.model.ProductDetail;
import com.hugo.commerce.domain.model.ProductOption;
import com.hugo.commerce.domain.model.ProductOptionDetail;
import com.hugo.commerce.support.Page;

import java.util.List;
import com.hugo.commerce.support.PageParam;
import com.hugo.commerce.support.error.CoreException;
import com.hugo.commerce.support.error.ErrorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductFinderTest {

    private ProductFinder productFinder;
    private FakeProductRepository productRepository;
    private FakeProductCategoryRepository productCategoryRepository;
    private FakeProductOptionRepository productOptionRepository;
    private FakeProductSectionRepository productSectionRepository;

    @BeforeEach
    void setUp() {
        productRepository = new FakeProductRepository();
        productCategoryRepository = new FakeProductCategoryRepository();
        productOptionRepository = new FakeProductOptionRepository();
        productSectionRepository = new FakeProductSectionRepository();
        productFinder = new ProductFinder(
            productRepository,
            productCategoryRepository,
            productOptionRepository,
            productSectionRepository
        );
    }

    @Test
    @DisplayName("카테고리에 속한 상품 목록 반환")
    void returnsProducts_whenCategoryHasProducts() {
        // given
        Long categoryId = 1L;
        Product product1 = ProductFixture.create(1L);
        Product product2 = ProductFixture.create(2L);
        productRepository.save(product1);
        productRepository.save(product2);
        productCategoryRepository.save(categoryId, 1L);
        productCategoryRepository.save(categoryId, 2L);

        // when
        Page<Product> result = productFinder.findProducts(categoryId, new PageParam(null, 10));

        // then
        assertThat(result.content()).containsExactly(product1, product2);
        assertThat(result.hasNext()).isFalse();
    }

    @Test
    @DisplayName("다음 페이지가 있으면 hasNext true, nextCursor 반환")
    void returnsHasNextTrue_whenMoreProductsExist() {
        // given
        Long categoryId = 1L;
        for (long i = 1; i <= 3; i++) {
            productRepository.save(ProductFixture.create(i));
            productCategoryRepository.save(categoryId, i);
        }

        // when
        Page<Product> result = productFinder.findProducts(categoryId, new PageParam(null, 2));

        // then
        assertThat(result.content()).hasSize(2);
        assertThat(result.hasNext()).isTrue();
    }

    @Test
    @DisplayName("카테고리에 상품이 없으면 빈 목록 반환")
    void returnsEmptyList_whenCategoryHasNoProducts() {
        // when
        Page<Product> result = productFinder.findProducts(1L, new PageParam(null, 10));

        // then
        assertThat(result.content()).isEmpty();
        assertThat(result.hasNext()).isFalse();
    }

    @Test
    @DisplayName("커서 이후의 상품만 반환")
    void returnsProductsAfterCursor() {
        // given
        Long categoryId = 1L;
        for (long i = 1; i <= 4; i++) {
            productRepository.save(ProductFixture.create(i));
            productCategoryRepository.save(categoryId, i);
        }

        // when
        Page<Product> result = productFinder.findProducts(categoryId, new PageParam(2L, 10));

        // then
        assertThat(result.content()).extracting(Product::id).containsExactly(3L, 4L);
        assertThat(result.hasNext()).isFalse();
    }

    @Test
    @DisplayName("상품 ID로 옵션, 섹션을 포함한 상세 정보 반환")
    void returnsProductDetail_whenProductExists() {
        // given
        Long productId = 1L;
        Product product = ProductFixture.create(productId);
        productRepository.save(product);
        productOptionRepository.save(productId, ProductOptionFixture.create(1L, productId));
        productSectionRepository.save(productId, ProductSectionFixture.create(ProductSectionType.IMAGE));

        // when
        ProductDetail result = productFinder.findProduct(productId);

        // then
        assertThat(result.product()).isEqualTo(product);
        assertThat(result.options()).hasSize(1);
        assertThat(result.sections()).hasSize(1);
    }

    @Test
    @DisplayName("옵션과 섹션이 없는 상품도 정상 반환")
    void returnsProductDetail_whenProductHasNoOptionsAndSections() {
        // given
        Product product = ProductFixture.create(1L);
        productRepository.save(product);

        // when
        ProductDetail result = productFinder.findProduct(1L);

        // then
        assertThat(result.product()).isEqualTo(product);
        assertThat(result.options()).isEmpty();
        assertThat(result.sections()).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 상품 ID로 조회하면 NOT_FOUND 예외 발생")
    void throwsNotFound_whenProductDoesNotExist() {
        // when & then
        assertThatThrownBy(() -> productFinder.findProduct(999L))
            .isInstanceOf(CoreException.class)
            .satisfies(e -> assertThat(((CoreException) e).getErrorType()).isEqualTo(ErrorType.NOT_FOUND));
    }

    @Test
    @DisplayName("INACTIVE 상품은 목록 조회에서 제외")
    void excludesInactiveProducts_whenFindingByCategoryId() {
        // given
        Long categoryId = 1L;
        productRepository.save(ProductFixture.create(1L));
        productRepository.save(ProductFixture.inactive(2L));
        productCategoryRepository.save(categoryId, 1L);
        productCategoryRepository.save(categoryId, 2L);

        // when
        Page<Product> result = productFinder.findProducts(categoryId, new PageParam(null, 10));

        // then
        assertThat(result.content()).extracting(Product::id).containsExactly(1L);
    }

    @Test
    @DisplayName("INACTIVE 상품 ID로 단건 조회하면 PRODUCT_UNAVAILABLE 예외 발생")
    void throwsProductUnavailable_whenProductIsInactive() {
        // given
        productRepository.save(ProductFixture.inactive(1L));

        // when & then
        assertThatThrownBy(() -> productFinder.findProduct(1L))
            .isInstanceOf(CoreException.class)
            .satisfies(e -> assertThat(((CoreException) e).getErrorType()).isEqualTo(ErrorType.PRODUCT_UNAVAILABLE));
    }

    @Test
    @DisplayName("옵션 ID로 옵션 단건 반환")
    void returnsProductOption_whenOptionExists() {
        // given
        productOptionRepository.save(1L, ProductOptionFixture.create(1L, 1L));

        // when
        ProductOption result = productFinder.findProductOption(1L);

        // then
        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("존재하지 않는 옵션 ID로 조회하면 NOT_FOUND 예외 발생")
    void throwsNotFound_whenProductOptionDoesNotExist() {
        // when & then
        assertThatThrownBy(() -> productFinder.findProductOption(999L))
            .isInstanceOf(CoreException.class)
            .satisfies(e -> assertThat(((CoreException) e).getErrorType()).isEqualTo(ErrorType.NOT_FOUND));
    }

    @Test
    @DisplayName("옵션 ID 목록으로 옵션 상세 정보 반환")
    void returnsProductOptionDetails_whenOptionsAndProductsExist() {
        // given
        productRepository.save(ProductFixture.create(1L));
        productOptionRepository.save(1L, ProductOptionFixture.create(1L, 1L));
        productOptionRepository.save(1L, ProductOptionFixture.create(2L, 1L));

        // when
        List<ProductOptionDetail> result = productFinder.findProductOptionDetails(List.of(1L, 2L));

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(d -> d.option().id()).containsExactly(1L, 2L);
    }

    @Test
    @DisplayName("옵션은 있지만 상품이 없으면 해당 옵션은 결과에서 제외")
    void excludesOptionDetail_whenProductDoesNotExist() {
        // given
        productOptionRepository.save(1L, ProductOptionFixture.create(1L, 1L));

        // when
        List<ProductOptionDetail> result = productFinder.findProductOptionDetails(List.of(1L));

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 옵션 ID는 결과에서 제외")
    void excludesMissingOptionIds_whenFindingOptionDetails() {
        // given
        productRepository.save(ProductFixture.create(1L));
        productOptionRepository.save(1L, ProductOptionFixture.create(1L, 1L));

        // when
        List<ProductOptionDetail> result = productFinder.findProductOptionDetails(List.of(1L, 999L));

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).option().id()).isEqualTo(1L);
    }
}
