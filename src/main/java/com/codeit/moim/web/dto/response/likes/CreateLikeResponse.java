package com.codeit.moim.web.dto.response.likes;

import com.codeit.moim.domain.Likes;
import lombok.Builder;

@Builder
public record CreateLikeResponse(int meetingId, int likesId) {
    public static CreateLikeResponse fromEntity(Likes likes){
        return CreateLikeResponse.builder()
                .meetingId(likes.getMeeting().getMeetingId())
                .likesId(likes.getLikesId())
                .build();
    }
}
