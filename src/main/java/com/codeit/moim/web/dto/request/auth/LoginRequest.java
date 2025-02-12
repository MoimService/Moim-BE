package com.codeit.moim.web.dto.request.auth;

public record LoginRequest (
        String email,
        String password
){
}
