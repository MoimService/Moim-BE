package com.codeit.moim.web.controller;

import com.codeit.moim.repository.CustomUserDetails;
import com.codeit.moim.service.comment.CommentService;
import com.codeit.moim.web.dto.request.comment.CreateCommentRequest;
import com.codeit.moim.web.dto.request.comment.ReadMeetingCommentRequest;
import com.codeit.moim.web.dto.request.comment.ReadMyCommentRequest;
import com.codeit.moim.web.dto.request.comment.UpdateCommentRequest;
import com.codeit.moim.web.dto.request.meeting.CreateMeetingRequest;
import com.codeit.moim.web.dto.response.Response;
import com.codeit.moim.web.dto.response.comment.*;
import com.codeit.moim.web.dto.response.meeting.CreateMeetingResponse;
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
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;
    @Operation(
            summary = "Create Comment",
            description = "Create Comment API. Return commentId. Only member can create comment"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create comment success")
    })
    @PostMapping("/{meetingId}")
    public Response<CreateCommentResponse> createComment(
            @PathVariable int meetingId,
            @Valid @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        int userId = userDetails.getUserId();
        return Response.ok(commentService.saveComment(userId, meetingId, request));
    }

    @Operation(
            summary = "Update Comment",
            description = "Update Comment API. Return commentId. Only member and user who wrote this comment can update comment"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update comment success")
    })
    @PutMapping("/{meetingId}")
    public Response<UpdateCommentResponse> updateComment(
            @PathVariable int meetingId,
            @Valid @RequestBody UpdateCommentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        int userId = userDetails.getUserId();
        return Response.ok(commentService.updateComment(userId, meetingId, request));
    }

    @Operation(
            summary = "Delete Comment",
            description = "Delete Comment API. Return userId and meetingId. Only member and user who wrote this comment can delete comment"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete comment success")
    })
    @DeleteMapping("/{meetingId}")
    public Response<DeleteCommentResponse> deleteComment(
            @PathVariable int meetingId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        int userId = userDetails.getUserId();
        return Response.ok(commentService.deleteComment(userId, meetingId));
    }

    @Operation(
            summary = "Get comment average",
            description = "Get comment average of meeting. Return until first decimal"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get success")
    })
    @GetMapping("/avg/{meetingId}")
    public Response<ReadCommentAverageResponse> readCommentAverage(
            @PathVariable int meetingId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        int userId = userDetails.getUserId();
        return Response.ok(commentService.getCommentAverage(userId, meetingId));
    }

    @Operation(
            summary = "Get comment distribution count",
            description = "Get comment count per comment score"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get success")
    })
    @GetMapping("/count/{meetingId}")
    public Response<ReadCommentDistributionResponse> readCommentDistribution(
            @PathVariable int meetingId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        int userId = userDetails.getUserId();
        return Response.ok(commentService.getCommentDistribution(userId, meetingId));
    }

    @Operation(
            summary = "Get meeting comment",
            description = "Get comment for each meeting, infinite scroll applied with min size 3"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get success")
    })
    @GetMapping("/{meetingId}")
    public Response<Slice<ReadMeetingCommentResponse>> readMeetingComemnts(
            @PathVariable int meetingId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute ReadMeetingCommentRequest request
    ) {
        int userId = userDetails.getUserId();
        return Response.ok(commentService.getMeetingComments(userId, meetingId, request));
    }

    @Operation(
            summary = "Get my comments",
            description = "Get my comment, infinite scroll applied with min size 3"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get success")
    })
    @GetMapping("/my")
    public Response<Slice<ReadMyCommentResponse>> readMyComemnts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute ReadMyCommentRequest request
            ) {
        int userId = userDetails.getUserId();
        return Response.ok(commentService.getMyComments(userId, request));
    }
}
