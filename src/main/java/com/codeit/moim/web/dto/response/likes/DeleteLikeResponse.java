package com.codeit.moim.web.dto.response.likes;

import com.codeit.moim.domain.Likes;
import lombok.Builder;

@Builder
public record DeleteLikeResponse(int meetingId) {
    public static DeleteLikeResponse fromEntity(int meetingId){
        return DeleteLikeResponse.builder()
                .meetingId(meetingId)
                .build();
    }
}
