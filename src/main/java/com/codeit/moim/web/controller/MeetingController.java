package com.codeit.moim.web.controller;

import com.codeit.moim.repository.CustomUserDetails;
import com.codeit.moim.service.meeting.MeetingService;
import com.codeit.moim.web.dto.request.meeting.CreateMeetingRequest;
import com.codeit.moim.web.dto.response.Response;
import com.codeit.moim.web.dto.response.meeting.CreateMeetingResponse;
import com.codeit.moim.web.dto.response.meeting.ReadTopMeetingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/meetings")
public class MeetingController {
    private final MeetingService meetingService;

    @Operation(
            summary = "Create Meeting",
            description = "Create Meeting API. Return meetingId"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "create meeting success")
    })
    @PostMapping
    public Response<CreateMeetingResponse> createMeeting(
            @Valid @RequestBody CreateMeetingRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        int userId = userDetails.getUserId();
        return Response.ok(meetingService.saveMeeting(userId, request));
    }


    @Operation(
            summary = "Get Meetings with most likes",
            description = "Get top 4 meetings with most likes API"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get meetings success")
    })
    @GetMapping("/top")
    public Response<List<ReadTopMeetingResponse>> getTopMeetingList(
            @RequestParam String categoryTitle,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        int userId = userDetails.getUserId();
        return Response.ok(meetingService.findTopMeetingList(userId, categoryTitle));
    }

}
