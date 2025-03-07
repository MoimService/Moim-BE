package com.codeit.moim.common.exception.global;

import com.codeit.moim.common.exception.payload.ErrorStatus;

import java.time.LocalDateTime;

public class BadRequestException extends ApplicationException {
    private static final String ACCESS_DENIED_EXCEPTION_MESSAGE = "BAD REQUEST";
    private static final int ACCESS_DENIED_EXCEPTION_STATUS_CODE = 400;
    private final String request;
    private final String entityType;

    public BadRequestException(String request, String entityType) {
        super(new ErrorStatus(ACCESS_DENIED_EXCEPTION_MESSAGE, ACCESS_DENIED_EXCEPTION_STATUS_CODE, LocalDateTime.now()));
        this.request = request;
        this.entityType = entityType;
    }

    public String getRequest() {
        return request;
    }

    public String getEntityType() {
        return entityType;
    }
}
