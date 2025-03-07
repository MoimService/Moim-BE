package com.codeit.moim.common.exception.meeting;

import com.codeit.moim.common.exception.global.EntityNotFoundException;

public class MeetingNotFoundException extends EntityNotFoundException {
    private static final String ENTITY_TYPE = "Meeting";
    public MeetingNotFoundException(String request) {
        super(request, ENTITY_TYPE);
    }
}
