package com.hugo.commerce.support.error;

public record ErrorMessage<T>(
    String code,
    String message,
    T data
) {

    public ErrorMessage(ErrorType errorType, T data) {
        this(errorType.name(), errorType.getMessage(), data);
    }

}
