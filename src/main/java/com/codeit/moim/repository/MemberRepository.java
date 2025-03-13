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

    boolean existsByUserEmailAndMeetingAndStatus(String email, Meeting meeting, MemberStatus memberStatus);

    @Query(
            "SELECT m.meeting FROM Member m " +
                    "JOIN FETCH m.meeting.category " +
                    "WHERE m.user = :user " +
                    "AND m.meeting.isPublic = :isPublic " +
                    "ORDER BY m.meeting.meetingId DESC "
    )
    Slice<Meeting> findByUser_userOrderByMeetingIdDesc(@Param("user") User user,  boolean isPublic, Pageable pageable);

    @Query(
            "SELECT m.meeting FROM Member m " +
                    "JOIN FETCH m.meeting.category " +
                    "WHERE m.user = :user " +
                    "AND m.meeting.isPublic = :isPublic " +
                    "AND m.meeting.meetingId < :lastMeetingId " +
                    "ORDER BY m.meeting.meetingId DESC "
    )
    Slice<Meeting> findByUser_userLessThanOrderByMeetingIdDesc(@Param("user") User user,  boolean isPublic, @Param("status") Integer lastMeetingId, Pageable pageable);
    @Query(
            "SELECT m FROM Member mb " +
                    "JOIN mb.meeting m " +
                    "JOIN FETCH mb.meeting.category " +
                    "WHERE mb.user = :user " +
                    "AND mb.status = :status " +
                    "AND NOT EXISTS (" +
                        "SELECT c FROM Comment c " +
                        "WHERE c.meeting = m " +
                        "AND c.user = :user " +
                    ")" +
                    "ORDER BY m.meetingId DESC "
    )

    Slice<Meeting> findByUserMemberStatus_NotExistsComments_OrderByMeetingIdDesc(User user, MemberStatus status, Pageable pageable);


    @Query(
            "SELECT m FROM Member mb " +
                    "JOIN mb.meeting m " +
                    "JOIN FETCH mb.meeting.category " +
                    "WHERE mb.user = :user " +
                    "AND mb.status = :status " +
                    "AND NOT EXISTS (" +
                    "SELECT c FROM Comment c " +
                    "WHERE c.meeting = m " +
                    "AND c.user = :user " +
                    ")" +
                    "AND m.meetingId < :lastMeetingId " +
                    "ORDER BY m.meetingId DESC "
    )
    Slice<Meeting> findByUserMemberStatus_NotExistsComments_LessThanOrderByMeetingIdDesc(User user, MemberStatus status, Integer lastMeetingId, Pageable pageable);

//    @Query(
//            "SELECT m.meeting FROM Member m " +
//                    "JOIN FETCH m.meeting.category " +
//                    "WHERE m.user = :user AND m.status = :status " +
//                    "ORDER BY m.meeting.meetingId DESC "
//    )
//    Slice<Meeting> findByUser_memberStatusOrderByMeetingIdDesc(@Param("user")User user, @Param("status") MemberStatus status, Pageable pageable);
//
//    @Query(
//            "SELECT m.meeting FROM Member m " +
//                    "JOIN FETCH m.meeting.category " +
//                    "WHERE m.user = :user AND m.status = :status " +
//                    "AND m.meeting.meetingId < :lastMeetingId " +
//                    "ORDER BY m.meeting.meetingId DESC "
//    )
//    Slice<Meeting> findByUser_memberStatusLessThanOrderByMeetingIdDesc(@Param("user") User user, @Param("status") MemberStatus status, Integer lastMeetingId, Pageable pageable);
}
