package com.codeit.moim.web.dto.response.meeting;

import com.codeit.moim.domain.Meeting;
import lombok.Builder;
import java.time.LocalDate;

@Builder
public record ReadMeetingDetailResponse(
        int meetingId,
        String categoryTitle,
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
        boolean isMeetingManager,

        String memberStatus,
        String[] meetingSkillArray
){
    public static ReadMeetingDetailResponse fromEntity(Meeting meeting, boolean isLike, boolean isMember, boolean isMeetingManager, String memberStatus,String[] meetingSkillArray) {
        return ReadMeetingDetailResponse.builder()
            .meetingId(meeting.getMeetingId())
                .categoryTitle(meeting.getCategory().getCategoryTitle())
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
                .isMeetingManager(isMeetingManager)
                .memberStatus(memberStatus)
                .meetingSkillArray(meetingSkillArray)
        .build();
    }
}
