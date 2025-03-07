package com.codeit.moim.common.exception.global;

import com.codeit.moim.common.exception.payload.ErrorStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StorageException extends ApplicationException {
    private static final String STORAGE_EXCEPTION_MESSAGE = "STORAGE UPLOAD EXCEPTION";
    private final int statusCode;
    private final String request;
    private final String entityType;

    public StorageException(int statusCode, String request, String entityType) {
        super(new ErrorStatus(STORAGE_EXCEPTION_MESSAGE, statusCode, LocalDateTime.now()));
        this.statusCode = statusCode;
        this.request = request;
        this.entityType = entityType;
    }
}
