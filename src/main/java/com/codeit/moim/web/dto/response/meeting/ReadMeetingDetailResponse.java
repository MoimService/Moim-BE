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
        int likesCount,
        String content,

        LocalDate startdate,
        Boolean requireApproval,
        Boolean isPublic,
        Boolean isLike,
        Boolean isMember,
        String[] meetingSkillArray
){
    public static ReadMeetingDetailResponse fromEntity(Meeting meeting, boolean isLike, boolean isMember, String[] meetingSkillArray) {
        return ReadMeetingDetailResponse.builder()
            .meetingId(meeting.getMeetingId())
            .title(meeting.getMeetingTitle())
            .thumbnail(meeting.getThumbnail())
            .location(meeting.getLocation())
            .memberCount(meeting.getMemberCount())
            .maxMember(meeting.getMaxMember())
                .likesCount(meeting.getLikesCount())
            .content(meeting.getContent())
            .startdate(meeting.getStartDate())
                .requireApproval(meeting.isRequireApproval())
                .isPublic(meeting.isPublic())
            .isLike(isLike)
            .isMember(isMember)
                .meetingSkillArray(meetingSkillArray)
        .build();
    }
}
