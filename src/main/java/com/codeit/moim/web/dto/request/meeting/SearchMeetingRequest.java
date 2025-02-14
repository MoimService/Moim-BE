package com.codeit.moim.web.dto.request.meeting;

import jakarta.validation.constraints.Min;

public record SearchMeetingRequest (

        String keyword,

        String[] skillArray,

        String filterField,
        int lastMeetingId,

        @Min(4)
        int size
){
}
