package com.codeit.moim.web.controller;

import com.codeit.moim.repository.CustomUserDetails;
import com.codeit.moim.service.mymeeting.MyMeetingService;
import com.codeit.moim.web.dto.request.likes.ReadLikeMeetingRequest;
import com.codeit.moim.web.dto.request.mymeeting.ReadMemberProfileRequest;
import com.codeit.moim.web.dto.request.mymeeting.UpdateMemberStatusRequest;
import com.codeit.moim.web.dto.response.Response;
import com.codeit.moim.web.dto.response.member.DeleteMemberResponse;
import com.codeit.moim.web.dto.response.mymeeting.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mymeetings")
public class MyMeetingController {

    private final MyMeetingService myMeetingService;

    @Operation(
            summary = "Update member status",
            description = "Meeting manager can update member status from 'PENDING' to 'APPROVED' or 'REJECTED'. " +
                    "setMemberStatus field should be given either in 'APPROVED' or 'REJECTED' " +
                    "Only meeting manager has the permission to change member status "
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update success")
    })
    @PutMapping("/member-status")
    public Response<UpdateMemberStatusResponse> updateMemberStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateMemberStatusRequest request
            ){
        int userId = userDetails.getUserId();
        return Response.ok(myMeetingService.updateMemberStatus(userId, request));
    }

    @Operation(
            summary = "Update member status to EXPEL",
            description = "Expel member from meeting, update member status to EXPEL"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update success")
    })
    @PutMapping("/expel")
    public Response<UpdateMemberToExpelResponse> updateMemberStatusToExpel(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateMemberStatusRequest request
    ){
        int userId = userDetails.getUserId();
        return Response.ok(myMeetingService.expelMember(userId, request));
    }

    @Operation(
            summary = "Get all my meetings ",
            description = "Get all meetings, including managing meetings and all status"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get meetings success")
    })
    @GetMapping("/all")
    public Response<List<ReadAllMeetingResponse>> getAllMyMeetingList(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        int userId = userDetails.getUserId();
        return Response.ok(myMeetingService.findAllMyMeeting(userId));
    }

    @Operation(
            summary = "Get managing meetings ",
            description = "Get meetings that user created"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get meetings success")
    })
    @GetMapping("/manage")
    public Response<List<ReadManageMeetingResponse>> getManageMeetingList(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        int userId = userDetails.getUserId();
        return Response.ok(myMeetingService.findManageMeeting(userId));
    }

    @Operation(
            summary = "Update isPublic field of meeting",
            description = "Only meeting manager can update isPublic of meeting"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update success")
    })
    @PutMapping("/isPublic/{meetingId}")
    public Response<UpdateMeetingIsPublicResponse> updateMeetingIsPublic(
            @PathVariable int meetingId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        int userId = userDetails.getUserId();
        return Response.ok(myMeetingService.updateIsPublic(userId, meetingId));
    }

    @Operation(
            summary = "Delete member and cancel meeting application",
            description = "Cancel application for meeting. Only possible when member status is 'PENDING'"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete member success")
    })
    @DeleteMapping("/cancel/{meetingId}")
    public Response<DeleteMemberResponse> deleteMeetingApply(
            @PathVariable int meetingId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        int userId = userDetails.getUserId();
        return Response.ok(myMeetingService.cancelMemberApply(userId, meetingId));
    }

    @Operation(
            summary = "Delete member and quit meeting",
            description = "Delete member data from meeting. Only possible when member status is 'APPROVED'"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete member success")
    })
    @DeleteMapping("/quit/{meetingId}")
    public Response<DeleteMemberResponse> deleteMeetingMember(
            @PathVariable int meetingId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        int userId = userDetails.getUserId();
        return Response.ok(myMeetingService.quitMeeting(userId, meetingId));
    }

    @Operation(
            summary = "Get liked meetings",
            description = "Get all meetings with my likes" +
                    " Infinite scroll with nextCursor "
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get meetings success")
    })
    @GetMapping("/likes")
    public Response<Slice<ReadLikeMeetingResponse>> getLikeMeetingList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute ReadLikeMeetingRequest request
    ){
        int userId = userDetails.getUserId();
        return Response.ok(myMeetingService.findLikeMeetings(userId, request));
    }


    @Operation(
            summary = "Get meeting member profile",
            description = "Get member profile in meeting"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get member profile success")
    })
    @GetMapping("/member-profile")
    public Response<ReadMemberProfileResponse> getMemberProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute ReadMemberProfileRequest request
    ){
        int userId = userDetails.getUserId();
        return Response.ok(myMeetingService.findMemberProfile(userId, request));
    }

}
