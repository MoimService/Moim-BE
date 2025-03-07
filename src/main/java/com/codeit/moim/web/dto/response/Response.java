package com.codeit.moim.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public record Response<T>(
        int statusCode,
        T data,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
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
