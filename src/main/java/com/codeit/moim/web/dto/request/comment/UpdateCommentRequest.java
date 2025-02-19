package com.codeit.moim.web.dto.request.comment;

import com.codeit.moim.domain.Comment;
import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateCommentRequest(
        @NotNull @Min(1) @Max(5)
        Integer score,
        @NotNull
        String content
){
}
