package com.codeit.moim.common.exception.jwt;

import com.codeit.moim.common.exception.global.JwtException;

public class JwtNotValidException extends JwtException {
    private static final String ENTITY_TYPE = "JWT";
    public JwtNotValidException(String request) {
        super(request, ENTITY_TYPE);
    }
}
