package com.codeit.moim.common.exception.comment;

import com.codeit.moim.common.exception.global.EntityNotFoundException;

public class CommentNotFoundException extends EntityNotFoundException {
    private static final String ENTITY_TYPE = "Comment";
    public CommentNotFoundException(String request) {
        super(request, ENTITY_TYPE);
    }
}
