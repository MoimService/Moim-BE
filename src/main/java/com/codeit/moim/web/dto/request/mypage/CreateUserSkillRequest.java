package com.codeit.moim.web.dto.request.mypage;

import com.codeit.moim.domain.Skill;
import com.codeit.moim.domain.User;
import com.codeit.moim.domain.UserSkill;

public record CreateUserSkillRequest (
        String[] skillArray
){
    public UserSkill toEntity(User user, Skill skill){
        return UserSkill.builder()
                .user(user)
                .skill(skill)
                .build();
    }
}
