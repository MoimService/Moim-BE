package com.codeit.moim.web.dto.response.mymeeting;

import com.codeit.moim.domain.User;
import lombok.Builder;

@Builder
public record ReadAllMeetingMemberResponse(
        int userId,
        String profilePic,
        String name
){
    public static ReadAllMeetingMemberResponse fromEntity(User user){
        return ReadAllMeetingMemberResponse.builder()
                .userId(user.getUserId())
                .profilePic(user.getProfilePic())
                .name(user.getName())
                .build();
    }
}
