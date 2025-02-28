package com.codeit.moim.web.dto.response.mymeeting;

import com.codeit.moim.domain.Meeting;
import lombok.Builder;

import java.util.List;

@Builder
public record ReadAllMeetingResponse(
        int meetingId,
        String title,
        String thumbnail,
        String location,
        int memberCount,
        int maxMember,
        int likesCount,
        String myMemberStatus,

        List<ReadAllMeetingMemberResponse> memberList

) {
    public static ReadAllMeetingResponse fromEntity(Meeting meeting, String myMemberStatus, List<ReadAllMeetingMemberResponse> memberList){
        return ReadAllMeetingResponse.builder()
                .meetingId(meeting.getMeetingId())
                .title(meeting.getMeetingTitle())
                .thumbnail(meeting.getThumbnail())
                .location(meeting.getLocation())
                .memberCount(meeting.getMemberCount())
                .maxMember(meeting.getMaxMember())
                .likesCount(meeting.getLikesCount())
                .myMemberStatus(myMemberStatus)
                .memberList(memberList)
                .build();
    }
}
