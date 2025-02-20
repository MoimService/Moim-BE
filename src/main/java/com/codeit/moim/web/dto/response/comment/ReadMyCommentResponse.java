package com.codeit.moim.web.dto.response.comment;

import com.codeit.moim.domain.Comment;
import com.codeit.moim.domain.Meeting;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record ReadMyCommentResponse(
        int commentId,
        int score,
        String content,
        LocalDateTime createdAt,
        int meetingId,
        String meetingTitle,
        String thumbnail,
        String location,
        int memberCount,
        int maxMember

){
    public static ReadMyCommentResponse fromEntity(Comment comment , Meeting meeting){
        return ReadMyCommentResponse.builder()
                .commentId(comment.getCommentId())
                .score(comment.getScore())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .meetingId(meeting.getMeetingId())
                .meetingTitle(meeting.getMeetingTitle())
                .thumbnail(meeting.getThumbnail())
                .location(meeting.getLocation())
                .memberCount(meeting.getMemberCount())
                .maxMember(meeting.getMaxMember())
                .build();
    }
}
