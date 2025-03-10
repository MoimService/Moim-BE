package com.codeit.moim.web.dto.response.comment;

import com.codeit.moim.domain.Comment;
import com.codeit.moim.domain.Meeting;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReadMyMeetingCommentResponse(
        int meetingId,
        String categoryTitle,
        String meetingTitle,
        String thumbnail,
        String location,
        int memberCount,
        int maxMember

){
    public static ReadMyMeetingCommentResponse fromEntity(Meeting meeting){
        return ReadMyMeetingCommentResponse.builder()
                .meetingId(meeting.getMeetingId())
                .categoryTitle(meeting.getCategory().getCategoryTitle())
                .meetingTitle(meeting.getMeetingTitle())
                .thumbnail(meeting.getThumbnail())
                .location(meeting.getLocation())
                .memberCount(meeting.getMemberCount())
                .maxMember(meeting.getMaxMember())
                .build();
    }
}
