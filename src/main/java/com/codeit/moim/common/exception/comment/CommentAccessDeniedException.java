package com.codeit.moim.common.exception.comment;

import com.codeit.moim.common.exception.global.AccessDeniedException;

public class CommentAccessDeniedException extends AccessDeniedException {
    private static final String ENTITY_TYPE = "Comment";

    public CommentAccessDeniedException(String request) {
        super(request, ENTITY_TYPE);
    }
}
