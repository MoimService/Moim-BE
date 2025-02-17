package com.codeit.moim.common.exception;

import com.codeit.moim.common.exception.payload.ErrorStatus;

import java.time.LocalDateTime;

public class AccessDeniedException extends ApplicationException{
    private static final String ACCESS_DENIED_EXCEPTION_MESSAGE = "Access Denied. Do not have permission.";
    private static final int ACCESS_DENIED_EXCEPTION_STATUS_CODE = 403;
    private final String request;
    private final String entityType;

    public AccessDeniedException(String request, String entityType) {
        super(new ErrorStatus(ACCESS_DENIED_EXCEPTION_MESSAGE, ACCESS_DENIED_EXCEPTION_STATUS_CODE, LocalDateTime.now()));
        this.request = request;
        this.entityType = entityType;
    }
}
