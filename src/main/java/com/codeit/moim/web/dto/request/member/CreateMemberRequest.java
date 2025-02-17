package com.codeit.moim.web.dto.request.member;

import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.Member;
import com.codeit.moim.domain.User;
import com.codeit.moim.domain.enums.MemberStatus;
import jakarta.validation.constraints.NotNull;

public record CreateMemberRequest (
        @NotNull
        String message

){
    public Member toEntity(User user, Meeting meeting, MemberStatus status){
        return Member.builder()
                .user(user)
                .meeting(meeting)
                .status(status)
                .message(this.message)
                .build();
    }
}
