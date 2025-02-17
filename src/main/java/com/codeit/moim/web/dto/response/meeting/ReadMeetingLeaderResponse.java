package com.codeit.moim.web.dto.response.meeting;

import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.Skill;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ReadMeetingLeaderResponse(
        int meetingId,
        String name,
        String profilePic,
        String email,
        String intro,
        String phone,

        List<Skill> skillList
){
    public ReadMeetingLeaderResponse fromEntity(Meeting meeting, List<Skill> skillList) {
        return ReadMeetingLeaderResponse.builder()
            .meetingId(meeting.getMeetingId())
            .name(meeting.getUser().getName())
            .profilePic(meeting.getUser().getProfilePic())
            .email(meeting.getUser().getEmail())
            .intro(meeting.getUser().getIntro())
            .phone(meeting.getUser().getContact().getPhone())
                .skillList(skillList)
        .build();
    }
}
