package com.codeit.moim.common.exception.meeting;

import com.codeit.moim.common.exception.global.ApplicationException;
import com.codeit.moim.common.exception.global.BadRequestException;
import com.codeit.moim.common.exception.payload.ErrorStatus;

public class MaxMemberUpdateException extends BadRequestException {
    private static final String ENTITY_TYPE = "Meeting";
    public MaxMemberUpdateException(String request) {
        super(request, ENTITY_TYPE);
    }
}
