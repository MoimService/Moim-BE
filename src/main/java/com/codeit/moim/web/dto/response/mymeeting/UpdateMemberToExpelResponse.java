package com.codeit.moim.web.dto.response.mymeeting;

import lombok.Builder;

@Builder
public record UpdateMemberToExpelResponse(
        int memberId
){
    public static UpdateMemberToExpelResponse fromEntity(int memberId){
        return UpdateMemberToExpelResponse.builder()
                .memberId(memberId)
                .build();
    }
}
