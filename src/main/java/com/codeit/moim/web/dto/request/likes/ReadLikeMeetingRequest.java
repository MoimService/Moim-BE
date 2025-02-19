package com.codeit.moim.web.dto.request.likes;

import jakarta.validation.constraints.Min;

public record ReadLikeMeetingRequest (
        Integer lastMeetingId,
        @Min(4)
        int size
){
}
