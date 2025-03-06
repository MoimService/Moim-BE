package com.codeit.moim.common.exception.likes;

import com.codeit.moim.common.exception.global.EntityNotFoundException;

public class LikeNotFoundException extends EntityNotFoundException {
    private static final String ENTITY_TYPE = "Likes";
    public LikeNotFoundException(String request) {
        super(request, ENTITY_TYPE);
    }
}
