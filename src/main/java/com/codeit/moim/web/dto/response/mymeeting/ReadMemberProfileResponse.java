package com.codeit.moim.web.dto.response.mymeeting;

        import com.codeit.moim.domain.Contact;
        import com.codeit.moim.domain.Member;
        import com.codeit.moim.domain.User;
        import lombok.Builder;

@Builder
public record ReadMemberProfileResponse (
        int userId,
        String name,
        String profilePic,
        String intro,
        String email,
        String position,
        String[] skillArray,
        String gender,
        String age,
        String location,
        ReadMemberContactResponse contactResponse,

        ReadMemberMessageResponse memberResponse

){
    public static ReadMemberProfileResponse fromEntity(User user, String[] skillArray, ReadMemberContactResponse contactResponse, ReadMemberMessageResponse memberResponse){
        return ReadMemberProfileResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .profilePic(user.getProfilePic())
                .intro(user.getIntro())
                .email(user.getEmail())
                .position(user.getPosition())
                .skillArray(skillArray)
                .gender(user.getGender())
                .age(user.getAge())
                .location(user.getLocation())
                .contactResponse(contactResponse)
                .memberResponse(memberResponse)
                .build();
    }
}
