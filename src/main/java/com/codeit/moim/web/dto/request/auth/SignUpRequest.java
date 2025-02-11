package com.codeit.moim.web.dto.request.auth;

import com.codeit.moim.domain.User;
import jakarta.validation.constraints.NotNull;

public record SignUpRequest (
        @NotNull
        String name,

        @NotNull
        String email,

        @NotNull
        String position,

        @NotNull
        String password,

        @NotNull
        String passwordCheck

){

    public User toEntity(String encodedPassword, String profilePic, String intro){
        return User.builder()
                .name(this.name)
                .email(this.email)
                .position(this.position)
                .password(encodedPassword)
                .profilePic(profilePic)
                .intro(intro)
                .gender("비공개")
                .location("선택 안함")
                .age("선택 안함")
                .build();
    }
}
