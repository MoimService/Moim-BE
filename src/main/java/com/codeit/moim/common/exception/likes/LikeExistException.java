package com.codeit.moim.common.exception.likes;

import com.codeit.moim.common.exception.EntityExistException;

public class LikeExistException extends EntityExistException {
    /**
     * @param request    엔티티를 찾기 위해 요청한 값
     * @param entityType 엔티티 타입 (User 등)
     */
    private static final String ENTITY_TYPE = "likes";
    public LikeExistException(String request) {
        super(request, ENTITY_TYPE);
    }
}
