package com.codeit.moim.common.exception.payload;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Builder
public record ErrorStatus(String message, int statusCode,
                          @JsonSerialize(using = LocalDateTimeSerializer.class)
                          @JsonDeserialize(using = LocalDateTimeDeserializer.class) LocalDateTime timestamp) {

    public static ErrorStatus toErrorStatus(String message, int statusCode) {
        return ErrorStatus.builder()
                .message(message)
                .statusCode(statusCode)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public HttpStatusCode toHttpStatus() {
        return HttpStatus.valueOf(statusCode);
    }
}
