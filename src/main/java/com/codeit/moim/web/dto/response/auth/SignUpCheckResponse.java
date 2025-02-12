package com.codeit.moim.web.dto.response.auth;

import lombok.Builder;

@Builder
public record SignUpCheckResponse(
        boolean ableToSignUp
){
}
