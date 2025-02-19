package com.codeit.moim.web.controller;

import com.codeit.moim.repository.CustomUserDetails;
import com.codeit.moim.service.likes.LikesService;
import com.codeit.moim.web.dto.response.Response;
import com.codeit.moim.web.dto.response.likes.CreateLikeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/meetings/{meetingId}/likes")
public class LikesController {
    private final LikesService likesService;

    @PostMapping
    public Response<CreateLikeResponse> createLikes(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable int meetingId
    ) {
        int userId = userDetails.getUserId();
        return Response.ok(likesService.createLikes(userId, meetingId));
    }
}
