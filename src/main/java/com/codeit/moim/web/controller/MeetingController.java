package com.codeit.moim.web.controller;

import com.codeit.moim.domain.User;
import com.codeit.moim.repository.CurrentUser;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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
//            @CurrentUser User user,
            @RequestParam String categoryTitle
            ){
        return Response.ok(meetingService.findTopMeetingList(categoryTitle));
    }

    @Operation(
            summary = "Search Meetings with category, keyword, skillList, filterField",
            description = "Get meeting that match search fields API"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get meetings success")
    })
    @GetMapping("/search")
    public Response<Slice<SearchMeetingResponse>> getSearchedMeeting(
            @RequestParam String categoryTitle,
            @Valid @RequestBody SearchMeetingRequest request
    ){
        return Response.ok(meetingService.findMeetingList(categoryTitle, request));
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
            @PathVariable int meetingId
    ){
        //int userId = userDetails.getUserId();
        //return Response.ok(meetingService.findMeetingDetail(meetingId, userId));
        return Response.ok(meetingService.findMeetingDetail(meetingId));

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
            @PathVariable int meetingId){
        return Response.ok(meetingService.findMeetingManagerDetail(meetingId));
    }
}
