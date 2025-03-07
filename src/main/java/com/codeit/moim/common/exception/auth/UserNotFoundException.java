package com.codeit.moim.common.exception.auth;

import com.codeit.moim.common.exception.global.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    private static final String ENTITY_TYPE = "User";
    public UserNotFoundException(String request) {
        super(request, ENTITY_TYPE);
    }
}
