package com.codeit.moim.common.exception.meeting;

import com.codeit.moim.common.exception.ApplicationException;
import com.codeit.moim.common.exception.payload.ErrorStatus;

public class AlreadyMemberException extends ApplicationException {
    /**
     * @param errorStatus 상태 코드, 메세지, 발생시간을 저장한 객체
     */
    public AlreadyMemberException(ErrorStatus errorStatus) {
        super(errorStatus);
    }
}
