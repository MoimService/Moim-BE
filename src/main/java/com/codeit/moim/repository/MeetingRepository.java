package com.codeit.moim.repository;

import com.codeit.moim.domain.Category;
import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Integer> {
    @Query(
            "SELECT m.user FROM Meeting m " +
                    "WHERE m = :meeting "
    )

    User findUserByMeeting(@Param("meeting") Meeting meeting);



    @Query(
            "SELECT DISTINCT m from Meeting m " +
                    "LEFT JOIN FETCH m.meetingSkillList ms " +
                    "LEFT JOIN FETCH ms.skill " +
                    "WHERE m.category.categoryTitle = :categoryTitle " +
                    "AND m.isPublic = :isPublic "
    )
    List<Meeting> findPublicMeetingsByCategory(@Param("categoryTitle") String categoryTitle, @Param("isPublic") boolean isPublic);

    @Query(
            "SELECT m FROM Meeting m " +
                    "JOIN FETCH m.user u " +
                    "LEFT JOIN FETCH u.userSkillList us " +
                    "LEFT JOIN FETCH us.skill s " +
                    "WHERE m.meetingId = :meetingId "
    )
    Optional<Meeting> findMeetingWithManagerAndSkill(@Param("meetingId") int meetingId);

    List<Meeting> findByUser(User user);
}
