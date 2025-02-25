package com.codeit.moim.repository;

import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.Member;
import com.codeit.moim.domain.User;
import com.codeit.moim.domain.enums.MemberStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

    boolean existsByUserAndMeetingAndStatus(@Param("user") User user, @Param("meeting") Meeting meeting, @Param("status") MemberStatus status);

    boolean existsByUserAndMeeting(User user, Meeting meeting);

    Member findByUserAndMeeting(User user, Meeting meeting);
    
    @Query(
            "SELECT m FROM Member m " +
                    "JOIN FETCH m.user " +
                    "WHERE m.meeting = :meeting "
    )
    List<Member> findByMeeting(Meeting meeting);

    @Query(
            "SELECT m.meeting FROM Member m " +
                    "WHERE m.user = :user " +
                    "ORDER BY m.meeting.meetingId DESC "
    )
    Slice<Meeting> findByUser_userOrderByMeetingIdDesc(@Param("user") User user, Pageable pageable);

    @Query(
            "SELECT m.meeting FROM Member m " +
                    "WHERE m.user = :user " +
                    "AND m.meeting.meetingId < :lastMeetingId " +
                    "ORDER BY m.meeting.meetingId DESC "
    )
    Slice<Meeting> findByUser_userLessThanOrderByMeetingIdDesc(@Param("user") User user, Integer lastMeetingId, Pageable pageable);

    boolean existsByUserEmailAndMeetingAndStatus(String email, Meeting meeting, MemberStatus memberStatus);
}
