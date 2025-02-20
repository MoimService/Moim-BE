package com.codeit.moim.repository;

import com.codeit.moim.domain.Likes;
import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Integer> {
    Boolean existsByUserAndMeeting(@Param("user") User user, @Param("meeting") Meeting meeting);

    Optional<Likes> findByUserAndMeeting(@Param("user") User user, @Param("meeting") Meeting meeting);

    @Query(
            "SELECT l.meeting FROM Likes l " +
                    "WHERE l.user = :user " +
                    "ORDER BY l.meeting.meetingId DESC "
    )
    Slice<Meeting> findLikedMeetings(@Param("user") User user, Pageable pageable);

    @Query(
            "SELECT l.meeting FROM Likes l " +
                    "WHERE l.user = :user " +
                    "AND l.meeting.meetingId < :lastMeetingId " +
                    "ORDER BY l.meeting.meetingId DESC"
    )
    Slice<Meeting> findLikeMeetingsLessThan(@Param("user") User user, @Param("lastLikeId") int lastMeetingId, Pageable pageable);
}
