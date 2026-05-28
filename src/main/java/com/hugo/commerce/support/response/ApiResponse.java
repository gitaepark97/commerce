package com.hugo.commerce.support.response;

import com.hugo.commerce.support.error.ErrorMessage;
import com.hugo.commerce.support.error.ErrorType;

public record ApiResponse<T>(
    ResultType result,
    T data,
    ErrorMessage<?> error
) {

    public static <T> ApiResponse<T> success() {
        return ApiResponse.success(null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResultType.SUCCESS, data, null);
    }

    public static <T> ApiResponse<T> error(ErrorType error) {
        return ApiResponse.error(error, null);
    }

    public static <T, E> ApiResponse<T> error(ErrorType error, E errorData) {
        return new ApiResponse<>(ResultType.ERROR, null, new ErrorMessage<>(error, errorData));
    }

}
