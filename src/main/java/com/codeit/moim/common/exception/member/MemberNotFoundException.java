package com.codeit.moim.common.exception.member;

import com.codeit.moim.common.exception.global.EntityNotFoundException;

public class MemberNotFoundException extends EntityNotFoundException {
    private static final String ENTITY_TYPE = "Member";
    public MemberNotFoundException(String request) {
        super(request, ENTITY_TYPE);
    }
}
