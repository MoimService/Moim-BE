package com.codeit.moim.web.dto.response;

import java.time.LocalDateTime;

public record Response<T>(
        int statusCode,
        T data,
        LocalDateTime timestamp
) {

    public Response(int statusCode, T data) {
        this(statusCode, data, LocalDateTime.now());
    }

    // 응답 시, 해당 메소드를 활용합니다.
    public static <T> Response<T> ok(T data) {
        return new Response<>(200, data);
    }
}
