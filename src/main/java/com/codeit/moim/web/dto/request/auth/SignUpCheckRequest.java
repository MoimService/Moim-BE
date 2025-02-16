package com.codeit.moim.web.dto.request.auth;

import jakarta.validation.constraints.NotNull;

public record SignUpCheckRequest(
        @NotNull
        String field
) {
}
