package com.codeit.moim.web.dto.request.meeting;

import com.codeit.moim.domain.Category;
import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.User;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CreateMeetingRequest (
        @NotNull
        String meetingTitle,
        @NotNull
        String categoryTitle,
        @NotNull
        String imageName,
        @NotNull
        String imageEncodedBase64,
        @NotNull
        String content,
        @NotNull
        String location,
        @NotNull
        int maxMember,
        @NotNull @FutureOrPresent
        LocalDate startDate,
        @NotNull
        boolean isPublic,
        @NotNull
        boolean enroll,
        String[] skillArray

){
    public Meeting toEntity(String uploadUrl, User user, Category category){
        return Meeting.builder()
                .meetingTitle(this.meetingTitle)
                .createdAt(LocalDateTime.now())
                .thumbnail(uploadUrl)
                .content(this.content)
                .location(this.location)
                .maxMember(this.maxMember)
                .startDate(this.startDate)
                .isPublic(this.isPublic)
                .enroll(this.enroll)
                .user(user)
                .category(category)
                .build();
    }
}
