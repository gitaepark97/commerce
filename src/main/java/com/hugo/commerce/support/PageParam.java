package com.hugo.commerce.support;

import com.hugo.commerce.support.error.CoreException;
import com.hugo.commerce.support.error.ErrorType;

public record PageParam(
    Long cursor,
    Integer size
) {

    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_SIZE = 100;

    public PageParam {
        if (size == null) size = DEFAULT_SIZE;
        if (size < 1 || size > MAX_SIZE) {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }
    }
    
}
