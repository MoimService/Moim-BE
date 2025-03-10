package com.codeit.moim.domain.enums;


import lombok.Getter;

@Getter
public enum SortField {
    OLD("오래된 순"),
    CREATED("생성 순"),
    LIKES("좋아요 순");

    private final String value;

    SortField(String value) {
        this.value = value;
    }
}
