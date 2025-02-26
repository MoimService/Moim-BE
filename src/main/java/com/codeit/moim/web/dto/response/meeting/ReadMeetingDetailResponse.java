package com.codeit.moim.web.dto.response.meeting;

import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.Skill;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ReadMeetingDetailResponse(
        int meetingId,
        String title,
        String thumbnail,
        String location,
        int memberCount,
        int maxMember,
        String content,

        LocalDate startdate,
        Boolean requireApproval,
        Boolean isLike,
        Boolean isMember,
        List<ReadMeetingSkillResponse> meetingSkillResponse
){
    public static ReadMeetingDetailResponse fromEntity(Meeting meeting, boolean isLike, boolean isMember,   List<ReadMeetingSkillResponse> meetingSkillResponse) {
        return ReadMeetingDetailResponse.builder()
            .meetingId(meeting.getMeetingId())
            .title(meeting.getMeetingTitle())
            .thumbnail(meeting.getThumbnail())
            .location(meeting.getLocation())
            .memberCount(meeting.getMemberCount())
            .maxMember(meeting.getMaxMember())
            .content(meeting.getContent())
            .startdate(meeting.getStartDate())
                .requireApproval(meeting.isRequireApproval())
            .isLike(isLike)
            .isMember(isMember)
                .meetingSkillResponse(meetingSkillResponse)
        .build();
    }
}
