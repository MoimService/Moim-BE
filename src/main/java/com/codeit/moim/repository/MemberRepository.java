package com.codeit.moim.repository;

import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.Member;
import com.codeit.moim.domain.enums.MemberStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    @Query(
           "SELECT COUNT(m) FROM Member m " +
                   "WHERE m.meeting = :meeting " +
                   "AND m.status = :memberStatus "
    )
    int countByMeetingAndStatus(@Param("meeting") Meeting meeting, @Param("memberStatus") MemberStatus memberStatus);
}
