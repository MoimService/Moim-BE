package com.codeit.moim.web.dto.response.mypage;

import com.codeit.moim.domain.User;
import lombok.Builder;

@Builder
public record ReadLoggedInUserResponse(
        int userId,
        String name,
        String email,
        String profilePic,
        String phone
){
    public static ReadLoggedInUserResponse fromEntity(User user, String phone){
        return ReadLoggedInUserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .profilePic(user.getProfilePic())
                .phone(phone)
                .build();
    }
}
