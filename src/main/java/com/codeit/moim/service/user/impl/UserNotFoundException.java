package com.codeit.moim.service.user.impl;

import com.codeit.moim.common.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    private static final String ENTITY_TYPE = "user";
    public UserNotFoundException(String request) {
        super(request, ENTITY_TYPE);
    }
}
