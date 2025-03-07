package com.codeit.moim.web.dto.response.meeting;

import com.codeit.moim.domain.Meeting;
import lombok.Builder;

@Builder
public record ReadTopMeetingResponse(
        int meetingId,
        String title,
        String thumbnail,
        String location,
        int memberCount,
        int maxMember,
        int likesCount,
        Boolean isLike

) {
    public static ReadTopMeetingResponse fromEntity(Meeting meeting, boolean isLike){
        return ReadTopMeetingResponse.builder()
                .meetingId(meeting.getMeetingId())
                .title(meeting.getMeetingTitle())
                .thumbnail(meeting.getThumbnail())
                .location(meeting.getLocation())
                .maxMember(meeting.getMaxMember())
                .memberCount(meeting.getMemberCount())
                .likesCount(meeting.getLikesCount())
                .isLike(isLike)
                .build();
    }
}
