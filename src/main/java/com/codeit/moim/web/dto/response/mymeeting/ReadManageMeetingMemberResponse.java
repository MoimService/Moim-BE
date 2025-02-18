package com.codeit.moim.web.dto.response.mymeeting;

import com.codeit.moim.domain.User;
import lombok.Builder;

import java.util.List;

@Builder
public record ReadManageMeetingMemberResponse(
        int userId,
        String profilePic,
        String name,
        String memberStatus
){
    public static ReadManageMeetingMemberResponse fromEntity(User user, String memberStatus){
        return ReadManageMeetingMemberResponse.builder()
                .userId(user.getUserId())
                .profilePic(user.getProfilePic())
                .name(user.getName())
                .memberStatus(memberStatus)
                .build();
    }
}
