package com.codeit.moim.web.dto.response.mymeeting;

import com.codeit.moim.domain.Meeting;
import lombok.Builder;

import java.util.List;

@Builder
public record ReadLikeMeetingResponse(
        int meetingId,
        String categoryTitle,
        String title,
        String thumbnail,
        String location,
        int memberCount,
        int maxMember,
        int likesCount
) {
    public static ReadLikeMeetingResponse fromEntity(Meeting meeting){
        return ReadLikeMeetingResponse.builder()
                .meetingId(meeting.getMeetingId())
                .categoryTitle(meeting.getCategory().getCategoryTitle())
                .title(meeting.getMeetingTitle())
                .thumbnail(meeting.getThumbnail())
                .location(meeting.getLocation())
                .memberCount(meeting.getMemberCount())
                .maxMember(meeting.getMaxMember())
                .likesCount(meeting.getLikesCount())
                .build();
    }
}
