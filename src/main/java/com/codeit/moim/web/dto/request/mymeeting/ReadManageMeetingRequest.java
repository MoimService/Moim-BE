package com.codeit.moim.web.dto.request.mymeeting;

import jakarta.validation.constraints.Min;

public record ReadManageMeetingRequest(
        Integer lastMeetingId,
        @Min(6)
        int size
){
}
