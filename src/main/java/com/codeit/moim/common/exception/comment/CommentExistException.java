package com.codeit.moim.common.exception.comment;

import com.codeit.moim.common.exception.global.EntityExistException;

public class CommentExistException extends EntityExistException {
    /**
     * @param request    엔티티를 찾기 위해 요청한 값
     * @param entityType 엔티티 타입 (User 등)
     */
    private static final String ENTITY_TYPE = "Comment";
    public CommentExistException(String request) {
        super(request, ENTITY_TYPE);
    }
}
