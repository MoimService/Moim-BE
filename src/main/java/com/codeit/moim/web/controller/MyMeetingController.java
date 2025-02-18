package com.codeit.moim.web.controller;

import com.codeit.moim.repository.CustomUserDetails;
import com.codeit.moim.service.mymeeting.MyMeetingService;
import com.codeit.moim.web.dto.request.mymeeting.UpdateMemberStatusRequest;
import com.codeit.moim.web.dto.request.mypage.UpdateProfilePicRequest;
import com.codeit.moim.web.dto.response.Response;
import com.codeit.moim.web.dto.response.mymeeting.UpdateMemberStatusResponse;
import com.codeit.moim.web.dto.response.mypage.UpdateProfilePicResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
