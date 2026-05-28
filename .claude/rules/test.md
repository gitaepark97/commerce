# Test Rules

## 테스트 메서드 네이밍

- 메서드명: **영어** (camelCase)
- 표시명: `@DisplayName`으로 **한국어** 작성

### 예시

```java
@Test
@DisplayName("유효하지 않은 요청 파라미터로 400 반환")
void returnsBadRequest_whenRequestParamIsInvalid() {
    ...
}
```

## 테스트 구조

given / when / then 구조를 따른다. 각 구간을 `// given`, `// when`, `// then` 인라인 주석으로 구분한다.
예외 검증처럼 when과 then을 분리하기 어려운 경우에는 `// when & then`으로 합친다.

### 예시

```java
@Test
@DisplayName("헤더에 유효한 사용자 ID가 있으면 User 반환")
void returnsUser_whenValidUserIdHeaderPresent() throws Exception {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Commerce-User-Id", "42");

    // when
    Object result = resolver.resolveArgument(null, null, new ServletWebRequest(request), null);

    // then
    assertThat(result).isEqualTo(new User(42L));
}

@Test
@DisplayName("헤더에 사용자 ID가 없으면 UNAUTHORIZED 예외 발생")
void throwsUnauthorized_whenUserIdHeaderMissing() {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();

    // when & then
    assertThatThrownBy(() -> resolver.resolveArgument(null, null, new ServletWebRequest(request), null))
        .isInstanceOf(CoreException.class);
}
```

## Web 레이어 테스트 (REST Docs)

- `RestDocsSupport`를 상속하여 작성한다.
- `get()`은 `RestDocumentationRequestBuilders`에서 import한다. (path parameter 캡처에 필요)
- 성공 케이스에 `andDo(document(...))` 를 추가하여 API 문서를 생성한다.
- query parameter는 `queryParameters`, path variable은 `pathParameters`, 응답은 `responseFields`로 문서화한다.
- 문서화한 스니펫은 `src/docs/asciidoc/index.adoc`에 include한다.

### 예시

```java
@Test
@DisplayName("상품 상세 조회 성공")
void returnsProductDetail_whenValidProductId() throws Exception {
    // given
    when(productService.getProduct(1L)).thenReturn(...);

    // when
    var result = mockMvc.perform(get("/api/v1/products/{productId}", 1L));

    // then
    result.andExpect(status().isOk())
        .andExpect(jsonPath("$.result").value("SUCCESS"))
        .andDo(document("products/detail",
            pathParameters(
                parameterWithName("productId").description("상품 ID")
            ),
            responseFields(
                fieldWithPath("result").description("결과 유형"),
                fieldWithPath("data.id").description("상품 ID"),
                fieldWithPath("error").optional().ignored()
            )
        ));
}
```

## Infra 레이어 테스트 (Testcontainers)

- `@RepositoryTest`를 사용한다. (`@SpringBootTest` + `TestcontainersConfiguration` + `@Transactional` 조합)
- 실제 DB(MySQL 컨테이너)를 사용하므로 mock 없이 JPA repository를 `@Autowired`로 주입한다.

### 예시

```java
@RepositoryTest
class ProductRepositoryImplTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Test
    @DisplayName("ID로 ACTIVE 상품 단건 조회")
    void findById_returnsActiveProduct() {
        // given
        productJpaRepository.save(EntityFixture.activeProduct(1L));

        // when
        Optional<Product> result = productRepository.findById(1L);

        // then
        assertThat(result).isPresent();
    }
}
```
