package com.codeit.moim.web.dto.response.mymeeting;

import com.codeit.moim.domain.Contact;
import lombok.Builder;

@Builder
public record ReadMemberContactResponse (
        String phone,
        String github,
        String kakao,
        String blog
){
    public static ReadMemberContactResponse fromEntity(Contact contact){
        return ReadMemberContactResponse
                .builder()
                .phone(contact.getPhone())
                .github(contact.getGithub())
                .kakao(contact.getKakao())
                .blog(contact.getBlog())
                .build();

    }
}
