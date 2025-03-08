package com.codeit.moim.web.dto.response.token;

import lombok.Builder;

public record JwtResponse (
        String accessToken,
        String refreshToken
){
    public JwtResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
