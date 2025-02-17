package com.codeit.moim.common.exception.meeting;

import com.codeit.moim.common.exception.AccessDeniedException;
import com.codeit.moim.common.exception.payload.ErrorStatus;
import com.rabbitmq.client.AMQP;

public class MeetingAccessDeniedException extends AccessDeniedException {
    private static final String ENTITY_TYPE = "meeting";

    public MeetingAccessDeniedException(String request) {
        super(request, ENTITY_TYPE);
    }
}
