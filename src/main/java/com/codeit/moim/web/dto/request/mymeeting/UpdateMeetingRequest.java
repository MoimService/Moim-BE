package com.codeit.moim.web.dto.request.mymeeting;

import com.codeit.moim.domain.Category;
import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.User;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UpdateMeetingRequest(

        String meetingTitle,
        String categoryTitle,
        String imageName,
        String imageEncodedBase64,
        String content,
        String location,
        int maxMember,
        @FutureOrPresent
        LocalDate startDate,
        boolean isPublic,
        boolean requireApproval

){
}
