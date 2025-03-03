package com.codeit.moim.web.dto.request.mymeeting;

import com.codeit.moim.domain.*;

public record UpdateMeetingSkillRequest(
        String[] skillArray
){
    public MeetingSkill toEntity(Meeting meeting, Skill skill){
        return MeetingSkill.builder()
                .meeting(meeting)
                .skill(skill)
                .build();
    }
}
