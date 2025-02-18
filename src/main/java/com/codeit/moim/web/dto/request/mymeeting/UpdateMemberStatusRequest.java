package com.codeit.moim.web.dto.request.mymeeting;

public record UpdateMemberStatusRequest (
        int userId,
        int meetingId,
        String setMemberStatus
){
}
