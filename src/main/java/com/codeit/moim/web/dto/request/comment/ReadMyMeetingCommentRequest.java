package com.codeit.moim.web.dto.request.comment;

import jakarta.validation.constraints.Min;

public record ReadMyMeetingCommentRequest(
        Integer lastMeetingId,
        @Min(3)
        int size
){

}
