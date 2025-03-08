package com.codeit.moim.web.dto.request.token;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public record TokenRefreshRequest (
        String refreshToken
){
}
