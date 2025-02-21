package com.codeit.moim.web.dto.request.mypage;

public record UpdateContactRequest(
        String phone,
        String kakao,
        String github,
        String blog
){}
