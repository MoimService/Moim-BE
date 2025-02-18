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
                .myMemberStatus(myMemberStatus)
                .memberList(memberList)
                .build();
    }
}
