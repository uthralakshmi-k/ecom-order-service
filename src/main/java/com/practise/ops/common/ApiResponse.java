package com.practise.ops.common;

import java.time.Instant;

public record ApiResponse<T>(
        String message,
        T data,
        Instant timestamp,
        int status
) {
        // Constructor with message + data (defaults timestamp = now, status = 200)
    public ApiResponse(String message, T data) {
        this(message, data, Instant.now(), 200);
    }

        // Constructor with data only (defaults message = "Success", timestamp = now, status = 200)
    public ApiResponse(T data) {
        this("Success", data, Instant.now(), 200);
    }

        // Constructor with data + status (defaults message = "Success", timestamp = now)
    public ApiResponse(T data, int status) {
        this("Success", data, Instant.now(), status);
    }

        // Constructor with message + data + status (defaults timestamp = now)
    public ApiResponse(String message, T data, int status) {
        this(message, data, Instant.now(), status);
    }
}
