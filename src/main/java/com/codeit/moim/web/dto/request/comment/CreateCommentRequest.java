package com.codeit.moim.web.dto.request.comment;

import com.codeit.moim.domain.Comment;
import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateCommentRequest (
        @NotNull(message = "Score cannot be null")
        @Min(value = 1, message = "Score must be at least 1")
        @Max(value = 5, message = "Score cannot be greater than 5")
        Integer score,
        @NotBlank(message = "Content cannot be empty")
        String content
){
    public Comment toEntity(User user, Meeting meeting){
        return Comment.builder()
                .score(this.score)
                .content(this.content)
                .createdAt(LocalDateTime.now())
                .user(user)
                .meeting(meeting)
                .build();
    }
}
