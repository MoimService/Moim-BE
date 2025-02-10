package com.codeit.moim.common.exception;

import com.codeit.moim.common.exception.payload.ErrorStatus;
import lombok.Getter;

/**
 * 어플리케이션에서 발생하는 예외를 나타내는 클래스입니다. 커스텀 예외 클래스의 최상위 예외 클래스입니다. (EntityNotFoundException 참고)
 * @see EntityNotFoundException
 */
@Getter

public class ApplicationException extends RuntimeException{
    private final ErrorStatus errorStatus;

    /**
     *
     * @param errorStatus 상태 코드, 메세지, 발생시간을 저장한 객체
     */
    public ApplicationException(ErrorStatus errorStatus) {
        this.errorStatus = errorStatus;
    }
}
