package com.codeit.moim.common.exception.member;

import com.codeit.moim.common.exception.global.EntityExistException;

public class MemberExistException extends EntityExistException {
    /**
     * @param errorStatus 상태 코드, 메세지, 발생시간을 저장한 객체
     */
    private static final String ENTITY_TYPE = "Member";
    public MemberExistException(String request) {
        super(request, ENTITY_TYPE);
    }
}
