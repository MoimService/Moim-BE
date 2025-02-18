package com.codeit.moim.web.dto.response.member;

import com.codeit.moim.domain.Member;
import com.codeit.moim.domain.enums.MemberStatus;
import lombok.Builder;

@Builder
public record DeleteMemberResponse(
        int memberId
){
}
