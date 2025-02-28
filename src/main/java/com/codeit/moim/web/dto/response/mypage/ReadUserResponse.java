package com.codeit.moim.web.dto.response.mypage;

import com.codeit.moim.domain.User;
import com.codeit.moim.web.dto.response.mymeeting.ReadMemberContactResponse;
import lombok.Builder;

@Builder
public record ReadUserResponse (
        int userId,
        String name,
        String profilePic,
        String intro,
        String email,
        String position,
        String gender,
        String age,
        String location,
        String[] skillArray,
        ReadMemberContactResponse contactResponse
){
    public static ReadUserResponse fromEntity(User user, String[] skillArray, ReadMemberContactResponse contactResponse){
        return ReadUserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .profilePic(user.getProfilePic())
                .intro(user.getIntro())
                .email(user.getEmail())
                .position(user.getPosition())
                .gender(user.getGender())
                .age(user.getAge())
                .location(user.getLocation())
                .skillArray(skillArray)
                .contactResponse(contactResponse)
                .build();
    }
}
