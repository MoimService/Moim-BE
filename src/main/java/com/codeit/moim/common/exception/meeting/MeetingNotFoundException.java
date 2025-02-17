package com.codeit.moim.common.exception.meeting;

import com.codeit.moim.common.exception.EntityNotFoundException;

public class MeetingNotFoundException extends EntityNotFoundException {
    private static final String ENTITY_TYPE = "meeting";
    public MeetingNotFoundException(String request) {
        super(request, ENTITY_TYPE);
    }
}
