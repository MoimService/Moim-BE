package com.codeit.moim.web.dto.response.comment;

import lombok.Builder;

@Builder
public record DeleteCommentResponse(
        int userId,
        int meetingId
){
    public static DeleteCommentResponse fromEntity(int userId, int meetingId){
        return DeleteCommentResponse.builder()
                .userId(userId)
                .meetingId(meetingId)
                .build();
    }
}
