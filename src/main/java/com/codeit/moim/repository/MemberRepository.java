package com.codeit.moim.repository;

import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.Member;
import com.codeit.moim.domain.User;
import com.codeit.moim.domain.enums.MemberStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

    boolean existsByUserAndMeetingAndStatus(@Param("user") User user, @Param("meeting") Meeting meeting, @Param("status") MemberStatus status);
}
