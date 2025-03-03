package com.codeit.moim.repository;

import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.MeetingSkill;
import com.codeit.moim.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface MeetingSkillRepository extends JpaRepository<MeetingSkill, Integer> {
    @Query(
            "SELECT ms FROM MeetingSkill ms " +
                    "JOIN FETCH ms.skill " +
                    "WHERE ms.meeting = :meeting "
    )
    List<MeetingSkill> findSkillByMeeting(Meeting meeting);

    void deleteAllByMeeting(Meeting meeting);
}
