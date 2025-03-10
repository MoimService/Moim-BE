package com.codeit.moim.web.dto.response.mymeeting;

import com.codeit.moim.domain.Meeting;
import lombok.Builder;

import java.util.List;

@Builder
public record ReadManageMeetingResponse(
        int meetingId,
        String categoryTitle,
        String title,
        String thumbnail,
        String location,
        int memberCount,
        int maxMember,
        int likesCount,
        boolean isPublic,
        List<ReadManageMeetingMemberResponse> memberList

) {
    public static ReadManageMeetingResponse fromEntity(Meeting meeting, List<ReadManageMeetingMemberResponse> memberList){
        return ReadManageMeetingResponse.builder()
                .meetingId(meeting.getMeetingId())
                .categoryTitle(meeting.getCategory().getCategoryTitle())
                .title(meeting.getMeetingTitle())
                .thumbnail(meeting.getThumbnail())
                .location(meeting.getLocation())
                .memberCount(meeting.getMemberCount())
                .maxMember(meeting.getMaxMember())
                .likesCount(meeting.getLikesCount())
                .isPublic(meeting.isPublic())
                .memberList(memberList)
                .build();
    }
}
