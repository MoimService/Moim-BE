package com.codeit.moim.web.controller;

import com.codeit.moim.repository.CustomUserDetails;
import com.codeit.moim.service.meeting.MeetingService;
import com.codeit.moim.web.dto.request.meeting.CreateMeetingRequest;
import com.codeit.moim.web.dto.request.meeting.SearchMeetingRequest;
import com.codeit.moim.web.dto.response.Response;
import com.codeit.moim.web.dto.response.meeting.*;
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

    @Operation(
            summary = "Search Meetings with category, keyword, skillList, filterField",
            description = "Get meeting that match search fields API"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get meetings success")
    })
    @GetMapping("/search")
    public Response<List<SearchMeetingResponse>> getSearchedMeeting(
            @RequestParam String categoryTitle,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody SearchMeetingRequest request
    ){
        int userId = userDetails.getUserId();
        return Response.ok(meetingService.findMeetingList(userId, categoryTitle, request));
    }

    @Operation(
            summary = "Get meeting details",
            description = "Get detail of the meeting, including isLike, isMember"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get meeting detail success")
    })
    @GetMapping("/detail/{meetingId}")
    public Response<ReadMeetingDetailResponse> getMeetingDetail(
            @PathVariable int meetingId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        int userId = userDetails.getUserId();
        return Response.ok(meetingService.findMeetingDetail(meetingId, userId));
    }

    @Operation(
            summary = "Get meeting manager detail",
            description = "Get manager detail with meetingId"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get meeting manager detail success")
    })
    @GetMapping("/detail/manager/{meetingId}")
    public Response<ReadMeetingManagerResponse> getMeetingManagerDetail(
            @PathVariable int meetingId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        int userId = userDetails.getUserId();
        return Response.ok(meetingService.findMeetingManagerDetail(meetingId, userId));
    }
}
