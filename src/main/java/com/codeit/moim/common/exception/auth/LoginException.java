package com.codeit.moim.common.exception.auth;

import com.codeit.moim.common.exception.global.BadRequestException;

public class LoginException extends BadRequestException {

    private static final String ENTITY_TYPE = "User";
    public LoginException(String request) {
        super(request, ENTITY_TYPE);
    }
}
