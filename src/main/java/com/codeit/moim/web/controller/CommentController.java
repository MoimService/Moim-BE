package com.codeit.moim.web.controller;

import com.codeit.moim.repository.CustomUserDetails;
import com.codeit.moim.service.comment.CommentService;
import com.codeit.moim.web.dto.request.comment.CreateCommentRequest;
import com.codeit.moim.web.dto.request.comment.UpdateCommentRequest;
import com.codeit.moim.web.dto.request.meeting.CreateMeetingRequest;
import com.codeit.moim.web.dto.response.Response;
import com.codeit.moim.web.dto.response.comment.CreateCommentResponse;
import com.codeit.moim.web.dto.response.comment.DeleteCommentResponse;
import com.codeit.moim.web.dto.response.comment.UpdateCommentResponse;
import com.codeit.moim.web.dto.response.meeting.CreateMeetingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments/{meetingId}")
public class CommentController {

    private final CommentService commentService;
    @Operation(
            summary = "Create Comment",
            description = "Create Comment API. Return commentId. Only member can create comment"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create comment success")
    })
    @PostMapping
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
    @PutMapping
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
    @DeleteMapping
    public Response<DeleteCommentResponse> deleteComment(
            @PathVariable int meetingId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        int userId = userDetails.getUserId();
        return Response.ok(commentService.deleteComment(userId, meetingId));
    }
}
