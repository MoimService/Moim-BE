package com.codeit.moim.web.dto.response.member;

import com.codeit.moim.domain.Member;
import com.codeit.moim.domain.enums.MemberStatus;
import lombok.Builder;

@Builder
public record CreateMemberResponse (
        int memberId,
        MemberStatus status
){
    public static CreateMemberResponse fromEntity(Member savedMember){
        return CreateMemberResponse.builder()
                .memberId(savedMember.getMemberId())
                .status(savedMember.getStatus())
                .build();
    }
}
