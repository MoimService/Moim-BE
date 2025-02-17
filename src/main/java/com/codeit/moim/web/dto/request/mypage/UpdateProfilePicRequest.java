package com.codeit.moim.web.dto.request.mypage;

public record UpdateProfilePicRequest (
        String profilePicBase64,
        String profilePicName
){
}
