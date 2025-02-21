package com.codeit.moim.web.dto.request.mypage;

public record UpdateUserRequest(

        String name,
        String intro,

        String position,
        String gender,
        String age,
        String location


){

}
