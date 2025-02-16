package com.codeit.moim.web.dto.request.auth;

import jakarta.validation.constraints.NotNull;

public record LoginRequest (
        @NotNull
        String email,
        @NotNull
        String password
){
}
