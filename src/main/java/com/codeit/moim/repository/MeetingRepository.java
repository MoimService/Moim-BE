package com.codeit.moim.repository;

import com.codeit.moim.domain.Category;
import com.codeit.moim.domain.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Integer> {
    List<Meeting> findByCategory(Category category);
}
