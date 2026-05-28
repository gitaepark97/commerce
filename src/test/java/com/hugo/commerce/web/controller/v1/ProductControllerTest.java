package com.hugo.commerce.web.controller.v1;

import com.hugo.commerce.domain.application.ProductService;
import com.hugo.commerce.domain.enums.ProductSectionType;
import com.hugo.commerce.domain.fixture.ProductFixture;
import com.hugo.commerce.domain.fixture.ProductOptionFixture;
import com.hugo.commerce.domain.fixture.ProductSectionFixture;
import com.hugo.commerce.domain.model.ProductDetail;
import com.hugo.commerce.support.Page;
import com.hugo.commerce.support.error.CoreException;
import com.hugo.commerce.support.error.ErrorType;
import com.hugo.commerce.web.controller.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest extends RestDocsSupport {

    private final ProductService productService = mock(ProductService.class);

    @Override
    protected Object initController() {
        return new ProductController(productService);
    }

    @Test
    @DisplayName("카테고리별 상품 목록 조회 성공")
    void returnsProducts_whenValidCategoryId() throws Exception {
        // given
        var products = List.of(ProductFixture.create(1L), ProductFixture.create(2L));
        when(productService.getProducts(eq(1L), any())).thenReturn(new Page<>(products, true));

        // when
        var result = mockMvc.perform(get("/api/v1/products")
            .param("categoryId", "1")
            .param("size", "2"));

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data.content").isArray())
            .andExpect(jsonPath("$.data.content.length()").value(2))
            .andExpect(jsonPath("$.data.content[0].id").value(1))
            .andExpect(jsonPath("$.data.content[0].status").value("ACTIVE"))
            .andExpect(jsonPath("$.data.hasNext").value(true))
            .andDo(document("products/list",
                queryParameters(
                    parameterWithName("categoryId").description("카테고리 ID"),
                    parameterWithName("cursor").optional().description("커서 (마지막 상품 ID)"),
                    parameterWithName("size").optional().description("페이지 크기 (기본값: 10, 최대: 100)")
                ),
                responseFields(
                    fieldWithPath("result").description("결과 유형"),
                    fieldWithPath("data.content[]").description("상품 목록"),
                    fieldWithPath("data.content[].id").description("상품 ID"),
                    fieldWithPath("data.content[].name").description("상품명"),
                    fieldWithPath("data.content[].thumbnailUrl").description("썸네일 URL"),
                    fieldWithPath("data.content[].shortDescription").description("짧은 설명"),
                    fieldWithPath("data.content[].salesPrice").description("판매가"),
                    fieldWithPath("data.content[].discountedPrice").description("할인가"),
                    fieldWithPath("data.content[].status").description("상품 상태 (ACTIVE, SOLD_OUT, INACTIVE)"),
                    fieldWithPath("data.hasNext").description("다음 페이지 존재 여부"),
                    fieldWithPath("error").optional().ignored()
                )
            ));
    }

    @Test
    @DisplayName("categoryId 파라미터 누락 시 400 반환")
    void returnsBadRequest_whenCategoryIdMissing() throws Exception {
        // when & then
        assertBadRequest(mockMvc.perform(get("/api/v1/products")));
    }

    @Test
    @DisplayName("유효하지 않은 size 파라미터로 400 반환")
    void returnsBadRequest_whenSizeIsInvalid() throws Exception {
        // when & then
        assertBadRequest(mockMvc.perform(get("/api/v1/products")
            .param("categoryId", "1")
            .param("size", "200")));
    }

    @Test
    @DisplayName("상품 상세 조회 성공")
    void returnsProductDetail_whenValidProductId() throws Exception {
        // given
        var product = ProductFixture.create(1L);
        var options = List.of(ProductOptionFixture.create(1L, 1L));
        var sections = List.of(ProductSectionFixture.create(ProductSectionType.IMAGE));
        when(productService.getProduct(1L)).thenReturn(new ProductDetail(product, options, sections));

        // when
        var result = mockMvc.perform(get("/api/v1/products/{productId}", 1L));

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data.id").value(1))
            .andExpect(jsonPath("$.data.options").isArray())
            .andExpect(jsonPath("$.data.options.length()").value(1))
            .andExpect(jsonPath("$.data.sections").isArray())
            .andExpect(jsonPath("$.data.sections.length()").value(1))
            .andExpect(jsonPath("$.data.sections[0].type").value("IMAGE"))
            .andDo(document("products/detail",
                pathParameters(
                    parameterWithName("productId").description("상품 ID")
                ),
                responseFields(
                    fieldWithPath("result").description("결과 유형"),
                    fieldWithPath("data.id").description("상품 ID"),
                    fieldWithPath("data.name").description("상품명"),
                    fieldWithPath("data.thumbnailUrl").description("썸네일 URL"),
                    fieldWithPath("data.description").description("상품 설명"),
                    fieldWithPath("data.shortDescription").description("짧은 설명"),
                    fieldWithPath("data.salesPrice").description("판매가"),
                    fieldWithPath("data.discountedPrice").description("할인가"),
                    fieldWithPath("data.status").description("상품 상태 (ACTIVE, SOLD_OUT, INACTIVE)"),
                    fieldWithPath("data.options[]").description("상품 옵션 목록"),
                    fieldWithPath("data.options[].id").description("옵션 ID"),
                    fieldWithPath("data.options[].name").description("옵션명"),
                    fieldWithPath("data.options[].description").description("옵션 설명"),
                    fieldWithPath("data.options[].salesPrice").description("옵션 판매가"),
                    fieldWithPath("data.options[].discountedPrice").description("옵션 할인가"),
                    fieldWithPath("data.options[].stockQuantity").description("재고 수량"),
                    fieldWithPath("data.sections[]").description("상품 섹션 목록"),
                    fieldWithPath("data.sections[].type").description("섹션 유형 (IMAGE, HTML)"),
                    fieldWithPath("data.sections[].content").description("섹션 내용"),
                    fieldWithPath("error").optional().ignored()
                )
            ));
    }

    @Test
    @DisplayName("존재하지 않는 상품 ID로 404 반환")
    void returnsNotFound_whenProductDoesNotExist() throws Exception {
        // given
        when(productService.getProduct(999L)).thenThrow(new CoreException(ErrorType.NOT_FOUND));

        // when
        var result = mockMvc.perform(get("/api/v1/products/{productId}", 999L));

        // then
        result.andExpect(status().isNotFound())
            .andExpect(jsonPath("$.result").value("ERROR"))
            .andExpect(jsonPath("$.error.code").value("NOT_FOUND"));
    }

    @Test
    @DisplayName("숫자가 아닌 상품 ID로 400 반환")
    void returnsBadRequest_whenProductIdIsNotNumber() throws Exception {
        // when & then
        assertBadRequest(mockMvc.perform(get("/api/v1/products/{productId}", "invalid")));
    }

    private void assertBadRequest(ResultActions result) throws Exception {
        result.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.result").value("ERROR"))
            .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));
    }

}
