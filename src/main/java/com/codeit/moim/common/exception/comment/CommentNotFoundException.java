package com.codeit.moim.common.exception.comment;

import com.codeit.moim.common.exception.EntityNotFoundException;

public class CommentNotFoundException extends EntityNotFoundException {
    private static final String ENTITY_TYPE = "comment";
    public CommentNotFoundException(String request) {
        super(request, ENTITY_TYPE);
    }
}
