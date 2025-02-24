package com.codeit.moim.web.dto.response.comment;

import com.codeit.moim.domain.Comment;
import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.User;
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
        String userName,
        String profilePic

){
    public static ReadMeetingCommentResponse fromEntity(Comment comment , Meeting meeting, User user){
        return ReadMeetingCommentResponse.builder()
                .commentId(comment.getCommentId())
                .score(comment.getScore())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .meetingId(meeting.getMeetingId())
                .userName(user.getName())
                .profilePic(user.getProfilePic())
                .build();
    }
}
