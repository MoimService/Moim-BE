package com.codeit.moim.common.exception.meeting;

import com.codeit.moim.common.exception.global.AccessDeniedException;

public class MeetingAccessDeniedException extends AccessDeniedException {
    private static final String ENTITY_TYPE = "Meeting";

    public MeetingAccessDeniedException(String request) {
        super(request, ENTITY_TYPE);
    }
}
