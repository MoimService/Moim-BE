package com.codeit.moim.common.exception.auth;

import com.codeit.moim.common.exception.EntityNotFoundException;

public class UserContactNotFoundException extends EntityNotFoundException {
    private static final String ENTITY_TYPE = "user_contact";
    public UserContactNotFoundException(String request) {
        super(request, ENTITY_TYPE);
    }
}
