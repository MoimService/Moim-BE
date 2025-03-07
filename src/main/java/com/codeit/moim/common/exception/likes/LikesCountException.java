package com.codeit.moim.common.exception.likes;

import com.codeit.moim.common.exception.global.BadRequestException;
import com.codeit.moim.common.exception.global.EntityExistException;

public class LikesCountException extends BadRequestException {
    private static final String ENTITY_TYPE = "Likes";
    public LikesCountException(String request) {
        super(request, ENTITY_TYPE);
    }
}
