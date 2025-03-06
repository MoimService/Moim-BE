package com.codeit.moim.common.exception.member;

import com.codeit.moim.common.exception.global.BadRequestException;

public class MemberCountException extends BadRequestException {

    private static final String ENTITY_TYPE = "Member";

    public MemberCountException(String request) {
        super(request, ENTITY_TYPE);
    }
}
