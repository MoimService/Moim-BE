package com.codeit.moim.common.exception.member;

import com.codeit.moim.common.exception.EntityExistException;

public class MemberCountException extends EntityExistException {
    /**
     * @param request    엔티티를 찾기 위해 요청한 값
     * @param entityType 엔티티 타입 (User 등)
     */
    public MemberCountException(String message, String request, String entityType) {
        super(message, request, entityType);
    }
}
