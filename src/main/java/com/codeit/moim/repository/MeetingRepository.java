package com.codeit.moim.repository;

import com.codeit.moim.domain.Category;
import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Integer> {


    @Query(
            "SELECT DISTINCT m from Meeting m " +
                    "JOIN FETCH m.user u " +
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


    @EntityGraph(attributePaths = {"category"})
    Slice<Meeting> findByUserOrderByMeetingIdDesc(User user, Pageable pageable);

    @EntityGraph(attributePaths = {"category"})
    Slice<Meeting> findByUserAndMeetingIdLessThanOrderByMeetingIdDesc(User user, Integer integer, Pageable pageable);

    @Query(
            "SELECT m FROM Meeting m " +
                    "JOIN FETCH m.user u " +
                    "JOIN FETCH m.category c " +
                    "WHERE m.meetingId = :meetingId "
    )
    Meeting findByIdWithUser(int meetingId);

    @Query(
            "SELECT m FROM Meeting m " +
                    "JOIN FETCH m.category " +
                    "WHERE m.user = :user " +
                    "ORDER BY m.meetingId DESC "
    )
    Slice<Meeting> findByUser_userOrderByMeetingIdDesc(@Param("user") User user, Pageable pageable);

    @Query(
            "SELECT m FROM Meeting m " +
                    "JOIN FETCH m.category " +
                    "WHERE m.user = :user " +
                    "AND m.meetingId < :lastMeetingId " +
                    "ORDER BY m.meetingId DESC "
    )
    Slice<Meeting> findByUser_userLessThanOrderByMeetingIdDesc(@Param("user") User user, Integer lastMeetingId, Pageable pageable);

}
