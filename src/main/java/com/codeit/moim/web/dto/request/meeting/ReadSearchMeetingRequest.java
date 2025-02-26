package com.codeit.moim.web.dto.request.meeting;

import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.RequestParam;

public record ReadSearchMeetingRequest(
        @RequestParam(required = false)
        Integer lastMeetingId,
        @RequestParam(defaultValue = "4")
        int size
){
}
