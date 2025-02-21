package com.codeit.moim.web.dto.request.mypage;

import jakarta.validation.constraints.NotNull;

public record UpdatePasswordRequest (

        @NotNull
        String currentPassword,
        @NotNull
        String newPassword,

        @NotNull
        String passwordCheck
){
}
