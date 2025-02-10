package com.codeit.moim.common.exception;

import com.codeit.moim.common.exception.payload.ErrorStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EntityNotFoundException extends ApplicationException{

    private static final String ENTITY_NOT_FOUND_EXCEPTION_MESSAGE = "해당하는 엔티티를 찾을 수 없습니다.";
    private static final int ENTITY_NOT_FOUND_EXCEPTION_STATUS_CODE = 404;

    private final String request;
    private final String entityType;

    /**
     *
     * @param request 엔티티를 찾기 위해 요청한 값
     * @param entityType 엔티티 타입 (User 등)
     */
    public EntityNotFoundException(String request, String entityType) {
        super(new ErrorStatus(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE, ENTITY_NOT_FOUND_EXCEPTION_STATUS_CODE, LocalDateTime.now()));
        this.request = request;
        this.entityType = entityType;
    }

}
