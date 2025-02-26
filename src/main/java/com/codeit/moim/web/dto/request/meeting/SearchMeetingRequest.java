package com.codeit.moim.web.dto.request.meeting;

import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.RequestParam;

public record SearchMeetingRequest (
        @RequestParam(required = false)

        String keyword,
        @RequestParam(required = false)

        String[] skillArray,
        @RequestParam(required = false)
        String sortField
){
}
