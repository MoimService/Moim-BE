package com.codeit.moim.web.dto.response.meeting;

import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.User;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record SearchMeetingResponse (
        int meetingId,
        String meetingTitle,
        String thumbnail,
        String location,
        int memberCount,
        int maxMember,
        LocalDate startDate,
        List<ReadMeetingSkillResponse> meetingSkillResponse,
        String name,
        String profilePic
){
    static public SearchMeetingResponse fromEntity(Meeting meeting, List<ReadMeetingSkillResponse> meetingSkillResponse, User user){
        return SearchMeetingResponse.builder()
                .meetingId(meeting.getMeetingId())
                .meetingTitle(meeting.getMeetingTitle())
                .thumbnail(meeting.getThumbnail())
                .location(meeting.getLocation())
                .memberCount(meeting.getMemberCount())
                .maxMember(meeting.getMaxMember())
                .startDate(meeting.getStartDate())
                .meetingSkillResponse(meetingSkillResponse)
                .name(user.getName())
                .profilePic(user.getProfilePic())
                .build();
    }
}
