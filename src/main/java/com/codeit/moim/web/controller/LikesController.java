package com.codeit.moim.web.controller;

import com.codeit.moim.repository.CustomUserDetails;
import com.codeit.moim.service.likes.LikesService;
import com.codeit.moim.web.dto.response.Response;
import com.codeit.moim.web.dto.response.likes.CreateLikeResponse;
import com.codeit.moim.web.dto.response.likes.DeleteLikeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/meetings/{meetingId}/likes")
public class LikesController {
    private final LikesService likesService;

    @Operation(
            summary = "Create likes with userId and meetingId",
            description = "Not possible when user is manager of meeting. " +
                    "Not possible when user already liked the meeting"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create success")
    })
    @PostMapping
    public Response<CreateLikeResponse> createLikes(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable int meetingId
    ) {
        int userId = userDetails.getUserId();
        return Response.ok(likesService.createLikes(userId, meetingId));
    }

    @Operation(
            summary = "Delete likes with userId and meetingId",
            description = "Delete existing like"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete success")
    })
    @DeleteMapping
    public Response<DeleteLikeResponse> deleteLikes(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable int meetingId
    ) {
        int userId = userDetails.getUserId();
        return Response.ok(likesService.deleteLikes(userId, meetingId));
    }
}
