package com.codeit.moim.repository;

import com.codeit.moim.domain.Category;
import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Integer> {
    List<Meeting> findByCategory(Category category);

    @Query(
         "SELECT m FROM Meeting m " +
                 "JOIN m.meetingSkillList ms " +
                 "JOIN ms.skill s " +
                 "WHERE s.skillTitle IN :skillList " +
                 "GROUP BY m.meetingId " +
                 "HAVING COUNT(DISTINCT s.skillTitle) = :size "

    )
    List<Meeting> findMeetingsBySkills(@Param("skillList")
                                       List<String> skillList,
                                       @Param("size") int size);

    @Query(
            "SELECT m.user FROM Meeting m " +
                    "WHERE m = :meeting "
    )

    User findUserByMeeting(@Param("meeting") Meeting meeting);



    @Query(
            "SELECT DISTINCT m from Meeting m " +
                    "LEFT JOIN FETCH m.meetingSkillList ms " +
                    "LEFT JOIN FETCH ms.skill " +
                    "WHERE m.category.categoryTitle = :categoryTitle "
    )
    List<Meeting> findMeetingsByCategory(@Param("categoryTitle") String categoryTitle);
}
