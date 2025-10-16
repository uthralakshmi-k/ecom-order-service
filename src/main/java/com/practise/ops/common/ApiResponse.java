package com.practise.ops.common;

import java.time.Instant;

public record ApiResponse<T>(
        String message,
        T data,
        Instant timestamp,
        int status
) {
    public ApiResponse(String message, T data) {
        this(message, data, Instant.now(), 200);
    }

}
