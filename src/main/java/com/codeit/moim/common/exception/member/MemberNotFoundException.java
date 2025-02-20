package com.codeit.moim.common.exception.member;

import com.codeit.moim.common.exception.EntityNotFoundException;

public class MemberNotFoundException extends EntityNotFoundException {
    private static final String ENTITY_TYPE = "member";
    public MemberNotFoundException(String request) {
        super(request, ENTITY_TYPE);
    }
}
