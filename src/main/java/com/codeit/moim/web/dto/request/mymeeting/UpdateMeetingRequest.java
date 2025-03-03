package com.codeit.moim.web.dto.request.mymeeting;

import com.codeit.moim.domain.*;
import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDate;

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
        boolean requireApproval,
        String[] skillArray

){
        public MeetingSkill toEntity(Meeting meeting, Skill skill) {
                return MeetingSkill.builder()
                        .meeting(meeting)
                        .skill(skill)
                        .build();
        }
}
