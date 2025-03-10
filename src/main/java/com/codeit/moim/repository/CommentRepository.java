package com.codeit.moim.repository;

import com.codeit.moim.domain.Comment;
import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    boolean existsByUserAndMeeting(@Param("user") User user, @Param("meeting") Meeting meeting);

    Optional<Comment> findByUserAndMeeting(@Param("user") User user, @Param("meeting") Meeting meeting);

    List<Comment> findByMeeting(@Param("meeting") Meeting meeting);


    @Query(
            "SELECT c FROM Comment c " +
                    "JOIN FETCH c.user " +
                    "WHERE c.meeting.meetingId = :meetingId " +
                    "ORDER BY c.commentId DESC "
    )
    Slice<Comment> findByMeeting_MeetingIdOrderByCommentIdDesc(@Param("meetingId") int meetingId, Pageable pageable);


    @Query(
            "SELECT c FROM Comment c " +
                    "JOIN FETCH c.user " +
                    "WHERE c.meeting.meetingId = :meetingId " +
                    "AND c.commentId < :lastCommentId " +
                    "ORDER BY c.commentId DESC "
    )
    Slice<Comment> findByMeeting_MeetingIdAndCommentIdLessThanOrderByCommentIdDesc(@Param("meetingId") int meetingId, @Param("lastCommentId") int lastCommentId, Pageable pageable);

    @Query(
            "SELECT c FROM Comment c " +
                    "JOIN FETCH c.meeting " +
                    "JOIN FETCH c.meeting.category " +
                    "WHERE c.user.userId = :userId " +
                    "ORDER BY c.commentId DESC "
    )
    Slice<Comment> findByUser_userIdOrderByCommentIdDesc(@Param("userId") int userId, Pageable pageable);

    @Query(
            "SELECT c FROM Comment c " +
                    "JOIN FETCH c.meeting " +
                    "JOIN FETCH c.meeting.category " +
                    "WHERE c.user.userId = :userId " +
                    "AND c.commentId < :lastCommentId " +
                    "ORDER BY c.commentId DESC "
    )
    Slice<Comment> findByUser_UserIdAndCommentIdLessThanOrderByCommentIdDesc(@Param("userId") int userId, @Param("lastCommentId") int lastCommentId, Pageable pageable);
}
