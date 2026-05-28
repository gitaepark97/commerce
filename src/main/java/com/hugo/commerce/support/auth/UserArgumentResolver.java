package com.hugo.commerce.support.auth;

import com.hugo.commerce.support.error.CoreException;
import com.hugo.commerce.support.error.ErrorType;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(User.class);
    }

    static final String USER_ID_HEADER = "Commerce-User-Id";

    @Override
    public @Nullable Object resolveArgument(
        @NonNull MethodParameter parameter,
        @Nullable ModelAndViewContainer mavContainer,
        @NonNull NativeWebRequest webRequest,
        @Nullable WebDataBinderFactory binderFactory
    ) throws Exception {
        var request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new CoreException(ErrorType.UNAUTHORIZED);
        }

        var userId = request.getHeader(USER_ID_HEADER);
        if (userId == null) {
            throw new CoreException(ErrorType.UNAUTHORIZED);
        }

        return new User(Long.parseLong(userId));
    }

}
