package com.codeit.moim.common.exception.likes;

import com.codeit.moim.common.exception.EntityNotFoundException;

public class LikeNotFoundException extends EntityNotFoundException {
    private static final String ENTITY_TYPE = "likes";
    public LikeNotFoundException(String request) {
        super(request, ENTITY_TYPE);
    }
}
