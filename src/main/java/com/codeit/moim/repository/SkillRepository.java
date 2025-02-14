package com.codeit.moim.repository;

import com.codeit.moim.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {
    Skill findByTitle(String skillTitle);
}
