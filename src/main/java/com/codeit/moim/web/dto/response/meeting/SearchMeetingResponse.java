package com.codeit.moim.web.dto.response.meeting;

import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.User;
import lombok.Builder;

@Builder
public record SearchMeetingResponse (
        int meetingId,
        String meetingTitle,
        String thumbnail,
        String location,
        int memberCount,
        int maxMember,
        String name,
        String profilePic
){
    static public SearchMeetingResponse fromEntity(Meeting meeting, int memberCount, User user){
        return SearchMeetingResponse.builder()
                .meetingId(meeting.getMeetingId())
                .meetingTitle(meeting.getMeetingTitle())
                .thumbnail(meeting.getThumbnail())
                .location(meeting.getLocation())
                .memberCount(memberCount)
                .maxMember(meeting.getMaxMember())
                .name(user.getName())
                .profilePic(user.getProfilePic())
                .build();
    }
}
