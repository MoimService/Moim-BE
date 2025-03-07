package com.codeit.moim.common.exception.auth;

import com.codeit.moim.common.exception.global.ApplicationException;
import com.codeit.moim.common.exception.global.BadRequestException;
import com.codeit.moim.common.exception.payload.ErrorStatus;

public class MeetingManagerException extends BadRequestException {

    private static final String ENTITY_TYPE = "Meeting";
    public MeetingManagerException(String request) {
        super(request, ENTITY_TYPE);
    }
}
