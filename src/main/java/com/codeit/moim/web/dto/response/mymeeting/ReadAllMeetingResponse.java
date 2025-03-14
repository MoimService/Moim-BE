package com.codeit.moim.web.dto.response.mymeeting;

import com.codeit.moim.domain.Meeting;
import lombok.Builder;

import java.util.List;

@Builder
public record ReadAllMeetingResponse(
        int meetingId,
        String categoryTitle,
        String title,
        String thumbnail,
        String location,
        int memberCount,
        int maxMember,
        int likesCount,
        String myMemberStatus,
        boolean isMeetingManager,

        List<ReadAllMeetingMemberResponse> memberList

) {
    public static ReadAllMeetingResponse fromEntity(Meeting meeting, String myMemberStatus, boolean isMeetingManager, List<ReadAllMeetingMemberResponse> memberList){
        return ReadAllMeetingResponse.builder()
                .meetingId(meeting.getMeetingId())
                .categoryTitle(meeting.getCategory().getCategoryTitle())
                .title(meeting.getMeetingTitle())
                .thumbnail(meeting.getThumbnail())
                .location(meeting.getLocation())
                .memberCount(meeting.getMemberCount())
                .maxMember(meeting.getMaxMember())
                .likesCount(meeting.getLikesCount())
                .myMemberStatus(myMemberStatus)
                .isMeetingManager(isMeetingManager)
                .memberList(memberList)
                .build();
    }
}
