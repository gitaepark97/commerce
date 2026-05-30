package com.hugo.commerce.web.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hugo.commerce.domain.application.CartService;
import com.hugo.commerce.domain.fixture.ProductFixture;
import com.hugo.commerce.domain.fixture.ProductOptionFixture;
import com.hugo.commerce.domain.model.Cart;
import com.hugo.commerce.domain.model.CartItem;
import com.hugo.commerce.domain.model.CartItemDetail;
import com.hugo.commerce.domain.model.ProductOptionDetail;
import com.hugo.commerce.support.auth.UserArgumentResolver;
import com.hugo.commerce.support.error.CoreException;
import com.hugo.commerce.support.error.ErrorType;
import com.hugo.commerce.web.controller.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CartControllerTest extends RestDocsSupport {

    private final CartService cartService = mock(CartService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected Object initController() {
        return new CartController(cartService);
    }

    @Override
    protected List<HandlerMethodArgumentResolver> argumentResolvers() {
        return List.of(new UserArgumentResolver());
    }

    @Test
    @DisplayName("장바구니 조회 성공")
    void returnsCart_whenUserExists() throws Exception {
        // given
        var product = ProductFixture.create(1L);
        var option = ProductOptionFixture.create(1L, 1L);
        var cartItem = new CartItem(10L, 1L, 1L, 1L, 2);
        var cart = new Cart(1L, List.of(new CartItemDetail(cartItem, new ProductOptionDetail(product, option))));
        when(cartService.getCart(1L)).thenReturn(cart);

        // when
        var result = mockMvc.perform(get("/api/v1/cart")
            .header(UserArgumentResolver.USER_ID_HEADER, "1"));

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data.items").isArray())
            .andExpect(jsonPath("$.data.items.length()").value(1))
            .andDo(document("cart/get",
                responseFields(
                    fieldWithPath("result").description("결과 유형"),
                    fieldWithPath("data.items[]").description("장바구니 아이템 목록"),
                    fieldWithPath("data.items[].id").description("장바구니 아이템 ID"),
                    fieldWithPath("data.items[].quantity").description("수량"),
                    fieldWithPath("data.items[].product.id").description("상품 ID"),
                    fieldWithPath("data.items[].product.name").description("상품명"),
                    fieldWithPath("data.items[].product.thumbnailUrl").description("썸네일 URL"),
                    fieldWithPath("data.items[].option.id").description("옵션 ID"),
                    fieldWithPath("data.items[].option.name").description("옵션명"),
                    fieldWithPath("data.items[].option.salesPrice").description("옵션 판매가"),
                    fieldWithPath("data.items[].option.discountedPrice").description("옵션 할인가"),
                    fieldWithPath("data.items[].option.isSoldOut").description("품절 여부"),
                    fieldWithPath("error").optional().ignored()
                )
            ));
    }

    @Test
    @DisplayName("장바구니가 비어있으면 빈 목록 반환")
    void returnsEmptyCart_whenCartIsEmpty() throws Exception {
        // given
        when(cartService.getCart(1L)).thenReturn(new Cart(1L, List.of()));

        // when & then
        mockMvc.perform(get("/api/v1/cart")
                .header(UserArgumentResolver.USER_ID_HEADER, "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.items").isEmpty());
    }

    @Test
    @DisplayName("장바구니 아이템 추가 성공")
    void addsCartItem_whenValidRequest() throws Exception {
        // given
        var body = Map.of("productId", 1, "productOptionId", 2, "quantity", 3);

        // when
        var result = mockMvc.perform(post("/api/v1/cart/items")
            .header(UserArgumentResolver.USER_ID_HEADER, "1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)));

        // then
        result.andExpect(status().isCreated())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andDo(document("cart/add-item",
                requestFields(
                    fieldWithPath("productId").description("상품 ID"),
                    fieldWithPath("productOptionId").description("상품 옵션 ID"),
                    fieldWithPath("quantity").description("수량 (최소 1)")
                ),
                responseFields(
                    fieldWithPath("result").description("결과 유형"),
                    fieldWithPath("data").optional().ignored(),
                    fieldWithPath("error").optional().ignored()
                )
            ));
        verify(cartService).addCartItem(eq(1L), any());
    }

    @Test
    @DisplayName("productId 없이 추가 시 400 반환")
    void returnsBadRequest_whenProductIdMissing() throws Exception {
        // given
        var body = Map.of("productOptionId", 2, "quantity", 3);

        // when & then
        mockMvc.perform(post("/api/v1/cart/items")
                .header(UserArgumentResolver.USER_ID_HEADER, "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));
    }

    @Test
    @DisplayName("quantity가 0이면 추가 시 400 반환")
    void returnsBadRequest_whenQuantityIsZeroOnAdd() throws Exception {
        // given
        var body = Map.of("productId", 1, "productOptionId", 2, "quantity", 0);

        // when & then
        mockMvc.perform(post("/api/v1/cart/items")
                .header(UserArgumentResolver.USER_ID_HEADER, "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));
    }

    @Test
    @DisplayName("재고 부족 시 아이템 추가 409 반환")
    void returnsConflict_whenStockInsufficient() throws Exception {
        // given
        var body = Map.of("productId", 1, "productOptionId", 2, "quantity", 100);
        doThrow(new CoreException(ErrorType.INSUFFICIENT_STOCK))
            .when(cartService).addCartItem(eq(1L), any());

        // when & then
        mockMvc.perform(post("/api/v1/cart/items")
                .header(UserArgumentResolver.USER_ID_HEADER, "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.error.code").value("INSUFFICIENT_STOCK"));
    }

    @Test
    @DisplayName("장바구니 아이템 수량 수정 성공")
    void modifiesCartItem_whenValidRequest() throws Exception {
        // given
        var body = Map.of("quantity", 5);

        // when
        var result = mockMvc.perform(patch("/api/v1/cart/items/{cartItemId}", 10L)
            .header(UserArgumentResolver.USER_ID_HEADER, "1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)));

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andDo(document("cart/modify-item",
                pathParameters(
                    parameterWithName("cartItemId").description("장바구니 아이템 ID")
                ),
                requestFields(
                    fieldWithPath("quantity").description("변경할 수량 (최소 1)")
                ),
                responseFields(
                    fieldWithPath("result").description("결과 유형"),
                    fieldWithPath("data").optional().ignored(),
                    fieldWithPath("error").optional().ignored()
                )
            ));
        verify(cartService).modifyCartItem(eq(1L), any());
    }

    @Test
    @DisplayName("quantity가 0이면 수정 시 400 반환")
    void returnsBadRequest_whenQuantityIsZeroOnModify() throws Exception {
        // given
        var body = Map.of("quantity", 0);

        // when & then
        mockMvc.perform(patch("/api/v1/cart/items/{cartItemId}", 10L)
                .header(UserArgumentResolver.USER_ID_HEADER, "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));
    }

    @Test
    @DisplayName("장바구니 아이템 삭제 성공")
    void removesCartItem_whenExists() throws Exception {
        // when
        var result = mockMvc.perform(delete("/api/v1/cart/items/{cartItemId}", 10L)
            .header(UserArgumentResolver.USER_ID_HEADER, "1"));

        // then
        result.andExpect(status().isNoContent())
            .andDo(document("cart/remove-item",
                pathParameters(
                    parameterWithName("cartItemId").description("장바구니 아이템 ID")
                )
            ));
        verify(cartService).removeCartItem(1L, 10L);
    }

    @Test
    @DisplayName("존재하지 않는 아이템 삭제 시 404 반환")
    void returnsNotFound_whenCartItemDoesNotExist() throws Exception {
        // given
        doThrow(new CoreException(ErrorType.NOT_FOUND))
            .when(cartService).removeCartItem(1L, 999L);

        // when & then
        mockMvc.perform(delete("/api/v1/cart/items/{cartItemId}", 999L)
                .header(UserArgumentResolver.USER_ID_HEADER, "1"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error.code").value("NOT_FOUND"));
    }

    @Test
    @DisplayName("인증 헤더 없으면 401 반환")
    void returnsUnauthorized_whenUserHeaderMissing() throws Exception {
        // when & then
        mockMvc.perform(get("/api/v1/cart"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.error.code").value("UNAUTHORIZED"));
    }

}
