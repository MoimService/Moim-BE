package com.codeit.moim.web.dto.request.mymeeting;

public record ReadMemberProfileRequest (
        int userId,
        int meetingId
){
}
