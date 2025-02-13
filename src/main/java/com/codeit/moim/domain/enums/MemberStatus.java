package com.codeit.moim.domain.enums;


import lombok.Getter;

@Getter
public enum MemberStatus {
    PENDING("대기"),
    APPROVED("승인"),
    REJECTED("거절"),
    EXPEL("강퇴");

    private final String value;

    MemberStatus(String value) {
        this.value = value;
    }
}
