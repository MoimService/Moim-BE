package com.codeit.moim.web.controller;

import com.codeit.moim.repository.CustomUserDetails;
import com.codeit.moim.service.mypage.MyPageService;
import com.codeit.moim.web.dto.request.member.CreateMemberRequest;
import com.codeit.moim.web.dto.request.mypage.UpdateProfilePicRequest;
import com.codeit.moim.web.dto.response.Response;
import com.codeit.moim.web.dto.response.member.CreateMemberResponse;
import com.codeit.moim.web.dto.response.mypage.UpdateProfilePicResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mypage")
public class MyPageController {
    private final MyPageService myPageService;

    @Operation(
            summary = "Update user profile picture",
            description = "Update picture with base64encoded image and picture name"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update success")
    })
    @PutMapping("/profilepic")
    public Response<UpdateProfilePicResponse> updateUserProfilePicture(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateProfilePicRequest request
            ){
        int userId = userDetails.getUserId();
        return Response.ok(myPageService.updateProfilePic(userId, request));
    }

}
