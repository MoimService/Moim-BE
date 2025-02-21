package com.codeit.moim.web.dto.response.mymeeting;

import com.codeit.moim.domain.Member;
import lombok.Builder;

@Builder
public record ReadMemberMessageResponse (
        int memberId,

        String message
){
    public static ReadMemberMessageResponse fromEntity(Member member){
        return ReadMemberMessageResponse.builder()
                .memberId(member.getMemberId())
                .message(member.getMessage())
                .build();
    }
}
