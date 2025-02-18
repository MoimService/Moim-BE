package com.codeit.moim.web.controller;

import com.codeit.moim.repository.CustomUserDetails;
import com.codeit.moim.service.meeting.MeetingService;
import com.codeit.moim.service.member.MemberService;
import com.codeit.moim.web.dto.request.member.CreateMemberRequest;
import com.codeit.moim.web.dto.response.Response;
import com.codeit.moim.web.dto.response.member.CreateMemberResponse;
import com.codeit.moim.web.dto.response.member.DeleteMemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;
    @Operation(
            summary = "Create member of meeting",
            description = "Apply to meeting with message. Member status will be 'PENDING' by default"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create meeting enroll success")
    })
    @PostMapping("/{meetingId}")
    public Response<CreateMemberResponse> createMeetingApply(
            @PathVariable int meetingId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CreateMemberRequest request
    ){
        int userId = userDetails.getUserId();
        return Response.ok(memberService.saveMember(meetingId, userId, request));
    }

    @Operation(
            summary = "Delete member application",
            description = "Cancel application for meeting. Only possible when member status is 'PENDING'"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete member success")
    })
    @DeleteMapping("/{meetingId}")
    public Response<DeleteMemberResponse> deleteMeetingApply(
            @PathVariable int meetingId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        int userId = userDetails.getUserId();
        return Response.ok(memberService.cancelMemberApply(userId, meetingId));
    }
}
