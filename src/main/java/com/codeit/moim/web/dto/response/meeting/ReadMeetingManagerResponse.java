package com.codeit.moim.web.dto.response.meeting;

import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.Skill;
import com.codeit.moim.domain.User;
import lombok.Builder;

import java.util.List;

@Builder
public record ReadMeetingManagerResponse(
        String name,
        String profilePic,
        String email,
        String intro,
        String phone,

        String[] skillArray
){
    public static ReadMeetingManagerResponse fromEntity(User user, String[] skillArray) {
        return ReadMeetingManagerResponse.builder()
            .name(user.getName())
            .profilePic(user.getProfilePic())
            .email(user.getEmail())
            .intro(user.getIntro())
            .phone(user.getContact().getPhone())
                .skillArray(skillArray)
        .build();
    }
}
