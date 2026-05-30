package com.hugo.commerce.support.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    DEFAULT_ERROR(500, "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", LogLevel.ERROR),
    BAD_REQUEST(400, "요청 파라미터가 올바르지 않습니다.", LogLevel.WARN),
    UNAUTHORIZED(401, "인증에 실패했습니다.", LogLevel.WARN),
    FORBIDDEN(403, "접근 권한이 없습니다.", LogLevel.WARN),
    NOT_FOUND(404, "요청한 리소스를 찾을 수 없습니다.", LogLevel.WARN),
    PRODUCT_UNAVAILABLE(410, "더 이상 판매하지 않는 상품입니다.", LogLevel.WARN),
    INSUFFICIENT_STOCK(409, "재고가 부족합니다.", LogLevel.WARN),
    METHOD_NOT_ALLOWED(405, "지원하지 않는 HTTP 메서드입니다.", LogLevel.WARN),
    ;

    private final int status;
    private final String message;
    private final LogLevel logLevel;
}
