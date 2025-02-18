package com.codeit.moim.common.exception.auth;

import com.codeit.moim.common.exception.ApplicationException;
import com.codeit.moim.common.exception.payload.ErrorStatus;

public class MeetingManagerException extends ApplicationException {
    /**
     * @param errorStatus 상태 코드, 메세지, 발생시간을 저장한 객체
     */
    public MeetingManagerException(ErrorStatus errorStatus) {
        super(errorStatus);
    }
}
