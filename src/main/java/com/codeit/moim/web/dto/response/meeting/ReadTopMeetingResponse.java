package com.codeit.moim.web.dto.response.meeting;

import com.codeit.moim.domain.Meeting;
import lombok.Builder;

@Builder
public record ReadTopMeetingResponse(
        int meetingId,
        String meetingTitle,
        String thumbnail,
        String location,
        int memberCount,
        int maxMember,
        Boolean isLike

) {
    public static ReadTopMeetingResponse fromEntity(Meeting meeting, int memberCount, boolean isLike){
        return ReadTopMeetingResponse.builder()
                .meetingId(meeting.getMeetingId())
                .meetingTitle(meeting.getMeetingTitle())
                .thumbnail(meeting.getThumbnail())
                .location(meeting.getLocation())
                .maxMember(meeting.getMaxMember())
                .memberCount(memberCount)
                .isLike(isLike)
                .build();
    }
}
