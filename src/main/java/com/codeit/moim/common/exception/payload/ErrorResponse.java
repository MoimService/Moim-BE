package com.codeit.moim.common.exception.payload;

import com.codeit.moim.common.exception.global.*;
import lombok.Builder;

@Builder
public record ErrorResponse (
        String errorMessage,
        String request,
        String entityType
){

    public ErrorResponse(String errorMessage) {
        this(errorMessage, null, null);
    }

    public static ErrorResponse fromError(ErrorStatus errorStatus, EntityExistException exception){
        return ErrorResponse.builder()
                .errorMessage(errorStatus.message())
                .request(exception.getRequest())
                .entityType(exception.getEntityType())
                .build();
    }

    public static ErrorResponse fromError(ErrorStatus errorStatus, AccessDeniedException exception){
        return ErrorResponse.builder()
                .errorMessage(errorStatus.message())
                .request(exception.getRequest())
                .entityType(exception.getEntityType())
                .build();
    }

    public static ErrorResponse fromError(ErrorStatus errorStatus, EntityNotFoundException exception){
        return ErrorResponse.builder()
                .errorMessage(errorStatus.message())
                .request(exception.getRequest())
                .entityType(exception.getEntityType())
                .build();
    }

    public static ErrorResponse fromError(ErrorStatus errorStatus, BadRequestException exception){
        return ErrorResponse.builder()
                .errorMessage(errorStatus.message())
                .request(exception.getRequest())
                .entityType(exception.getEntityType())
                .build();
    }

    public static ErrorResponse fromError(ErrorStatus errorStatus, JwtException exception){
        return ErrorResponse.builder()
                .errorMessage(errorStatus.message())
                .request(exception.getRequest())
                .entityType("JWT")
                .build();
    }


}
