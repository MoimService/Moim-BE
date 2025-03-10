package com.codeit.moim.repository;

import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.Member;
import com.codeit.moim.domain.User;
import com.codeit.moim.domain.enums.MemberStatus;
import io.lettuce.core.dynamic.annotation.Param;
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
}
