package com.codeit.moim.web.dto.response.mymeeting;

import com.codeit.moim.domain.Meeting;
import lombok.Builder;

@Builder
public record UpdateMeetingIsPublicResponse (
        int meetingId,
        boolean isPublic
){
    public static UpdateMeetingIsPublicResponse fromEntity(Meeting meeting){
        return UpdateMeetingIsPublicResponse.builder()
                .meetingId(meeting.getMeetingId())
                .isPublic(meeting.isPublic())
                .build();
    }
}
