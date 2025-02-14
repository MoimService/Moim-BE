package com.codeit.moim.repository;

import com.codeit.moim.domain.MeetingSkill;
import com.codeit.moim.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingSkillRepository extends JpaRepository<MeetingSkill, Integer> {
}
