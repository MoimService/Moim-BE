package com.codeit.moim.repository;

import com.codeit.moim.domain.User;
import com.codeit.moim.domain.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, Integer> {

    @Query(
            "SELECT us from UserSkill us " +
                    "JOIN FETCH us.skill " +
                    "WHERE us.user = :requestedUser"
    )
    List<UserSkill> findByUserWithSkill(User requestedUser);
}
