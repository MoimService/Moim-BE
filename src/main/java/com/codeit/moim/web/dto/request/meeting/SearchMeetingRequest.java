package com.codeit.moim.web.dto.request.meeting;

import jakarta.validation.constraints.Min;

import java.util.List;

public record SearchMeetingRequest (
        String keyword,

        List<String> skillArray,
        String sortField,

        Integer lastMeetingId,
        @Min(4)
        int size
){
}
