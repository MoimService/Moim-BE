package com.codeit.moim.web.dto.response.token;

import lombok.Builder;

public record JwtResponse (
        String accessToken,
        String responseToken
){
    public JwtResponse(String accessToken, String responseToken) {
        this.accessToken = accessToken;
        this.responseToken = responseToken;
    }
}
