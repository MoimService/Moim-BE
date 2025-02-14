package com.codeit.moim.repository;

import com.codeit.moim.domain.Likes;
import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Integer> {
    int countByMeeting(Meeting meeting);

    Boolean existsByUserAndMeeting(User user, Meeting meeting);
}
