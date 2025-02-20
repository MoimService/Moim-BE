package com.codeit.moim.repository;

import com.codeit.moim.domain.Comment;
import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    boolean existsByUserAndMeeting(@Param("user") User user, @Param("meeting") Meeting meeting);

    Optional<Comment> findByUserAndMeeting(@Param("user") User user, @Param("meeting") Meeting meeting);

    List<Comment> findByMeeting(Meeting meeting);
}
