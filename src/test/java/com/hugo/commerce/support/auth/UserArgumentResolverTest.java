package com.hugo.commerce.support.auth;

import com.hugo.commerce.support.error.CoreException;
import com.hugo.commerce.support.error.ErrorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserArgumentResolverTest {

    private UserArgumentResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new UserArgumentResolver();
    }

    @Test
    @DisplayName("User 타입 파라미터를 지원")
    void supportsParameter_whenParameterTypeIsUser() throws NoSuchMethodException {
        // given
        MethodParameter parameter = methodParameter("withUser");

        // when
        boolean result = resolver.supportsParameter(parameter);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("User 타입이 아닌 파라미터는 미지원")
    void doesNotSupportParameter_whenParameterTypeIsNotUser() throws NoSuchMethodException {
        // given
        MethodParameter parameter = methodParameter("withLong");

        // when
        boolean result = resolver.supportsParameter(parameter);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("헤더에 유효한 사용자 ID가 있으면 User 반환")
    void returnsUser_whenValidUserIdHeaderPresent() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(UserArgumentResolver.USER_ID_HEADER, "42");

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
            .isInstanceOf(CoreException.class)
            .satisfies(e -> assertThat(((CoreException) e).getErrorType()).isEqualTo(ErrorType.UNAUTHORIZED));
    }

    private MethodParameter methodParameter(String methodName) throws NoSuchMethodException {
        return switch (methodName) {
            case "withUser" -> new MethodParameter(Stub.class.getMethod("withUser", User.class), 0);
            case "withLong" -> new MethodParameter(Stub.class.getMethod("withLong", Long.class), 0);
            default -> throw new IllegalArgumentException("Unknown method: " + methodName);
        };
    }

    static class Stub {
        public void withUser(User user) {}
        public void withLong(Long id) {}
    }
}
