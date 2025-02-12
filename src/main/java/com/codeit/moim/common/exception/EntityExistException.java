package com.codeit.moim.common.exception;

import com.codeit.moim.common.exception.payload.ErrorStatus;

import java.time.LocalDateTime;

public class EntityExistException extends ApplicationException{
    private static final String ENTITY_NOT_FOUND_EXCEPTION_MESSAGE = "Entity already exists";
    private static final int ENTITY_NOT_FOUND_EXCEPTION_STATUS_CODE = 409;

    private final String request;
    private final String entityType;

    /**
     *
     * @param request 엔티티를 찾기 위해 요청한 값
     * @param entityType 엔티티 타입 (User 등)
     */
    public EntityExistException(String request, String entityType) {
        super(new ErrorStatus(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE, ENTITY_NOT_FOUND_EXCEPTION_STATUS_CODE, LocalDateTime.now()));
        this.request = request;
        this.entityType = entityType;
    }
}
