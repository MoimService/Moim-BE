package com.codeit.moim.web.dto.response.mymeeting;

import com.codeit.moim.domain.Member;
import lombok.Builder;

@Builder
public record UpdateMemberStatusResponse (
        int memberId,
        String memberStatus
){
    public static UpdateMemberStatusResponse fromEntity(Member member){
       return UpdateMemberStatusResponse.builder()
               .memberId(member.getMemberId())
               .memberStatus(member.getStatus().toString())
               .build();
    }
}
