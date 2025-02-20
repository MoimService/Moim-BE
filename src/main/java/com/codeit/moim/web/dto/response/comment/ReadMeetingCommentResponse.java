package com.codeit.moim.web.dto.response.comment;

import com.codeit.moim.domain.Comment;
import com.codeit.moim.domain.Meeting;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record ReadMeetingCommentResponse(
        int commentId,

        int score,
        String content,
        LocalDateTime createdAt,
        int meetingId,
        String meetingTitle,
        String location,
        LocalDate startDate,
        String userName

){
    public static ReadMeetingCommentResponse fromEntity(Comment comment , Meeting meeting, String userName){
        return ReadMeetingCommentResponse.builder()
                .commentId(comment.getCommentId())
                .score(comment.getScore())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .meetingId(meeting.getMeetingId())
                .meetingTitle(meeting.getMeetingTitle())
                .location(meeting.getLocation())
                .startDate(meeting.getStartDate())
                .userName(userName)
                .build();
    }
}
