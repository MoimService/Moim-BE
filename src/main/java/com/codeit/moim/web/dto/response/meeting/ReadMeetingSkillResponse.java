package com.codeit.moim.web.dto.response.meeting;

import com.codeit.moim.domain.Skill;
import lombok.Builder;

@Builder
public record ReadMeetingSkillResponse (
    String skillTitle
){
    public static ReadMeetingSkillResponse fromEntity(Skill skill){
        return ReadMeetingSkillResponse.builder()
                .skillTitle(skill.getSkillTitle())
                .build();
    }
}
