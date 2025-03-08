package com.codeit.moim.common.handler;

import com.codeit.moim.common.exception.global.*;
import com.codeit.moim.common.exception.payload.ErrorResponse;
import com.codeit.moim.common.exception.payload.ErrorStatus;
import com.codeit.moim.web.dto.response.Response;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalRestControllerAdvice {
    /**
     * ApplicationException 예외를 상속받는 모든 예외를 처리하는 메소드입니다.
     * 예외 처리 시, HTTP 상태 코드와 오류 정보를 포함한 응답을 반환합니다.
     *
     * @param e 발생한 예외
     * @return 해당 HTTP 상태 코드와 오류 정보를 반환
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Response<ErrorResponse>> handleApplicationException(ApplicationException e) {
        ErrorStatus errorStatus = e.getErrorStatus();


        ErrorResponse errorResponse;
        if (e instanceof EntityExistException exception) {
            errorResponse = ErrorResponse.fromError(errorStatus, exception);
        } else if (e instanceof AccessDeniedException exception) {
            errorResponse = ErrorResponse.fromError(errorStatus, exception);
        } else if (e instanceof EntityNotFoundException exception) {
            errorResponse = ErrorResponse.fromError(errorStatus, exception);
        } else if (e instanceof BadRequestException exception) {
            errorResponse = ErrorResponse.fromError(errorStatus, exception);
        } else if (e instanceof JwtException exception) {
            errorResponse = ErrorResponse.fromError(errorStatus, exception);
        } else if (e instanceof StorageException exception) {
            errorResponse = ErrorResponse.fromError(errorStatus, exception);
        }else {
            errorResponse = new ErrorResponse(errorStatus.message());
        }

        return ResponseEntity
                .status(errorStatus.statusCode())
                .body(new Response<>(errorStatus.statusCode(), errorResponse));
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<String> handleServletException(ServletException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED); // 401 상태 코드
    }

}
