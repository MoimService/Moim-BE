package com.codeit.moim.common.exception.global;

import com.codeit.moim.common.exception.payload.ErrorStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class JwtException extends ApplicationException {
    private static final String JWT_EXCEPTION_MESSAGE = "JWT EXCEPTION";
    private static final int JWT_EXCEPTION_STATUS_CODE = 401;

    private final String request;
    private final String entityType;



    public JwtException(String request, String entityType) {
        super(new ErrorStatus(JWT_EXCEPTION_MESSAGE, JWT_EXCEPTION_STATUS_CODE, LocalDateTime.now()));
        this.request = request;
        this.entityType = entityType;
    }
}
