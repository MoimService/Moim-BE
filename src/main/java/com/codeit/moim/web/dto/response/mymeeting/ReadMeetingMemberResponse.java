package com.codeit.moim.web.dto.response.mymeeting;

import com.codeit.moim.domain.User;
import lombok.Builder;

@Builder
public record ReadMeetingMemberResponse(
        int userId,
        String name
){
    public static ReadMeetingMemberResponse fromEntity(User user){
        return ReadMeetingMemberResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .build();
    }
}
