package com.codeit.moim.web.controller;

import com.codeit.moim.repository.CustomUserDetails;
import com.codeit.moim.service.mypage.MyPageService;
import com.codeit.moim.web.dto.request.comment.ReadMyCommentRequest;
import com.codeit.moim.web.dto.request.comment.ReadMyMeetingCommentRequest;
import com.codeit.moim.web.dto.request.mypage.*;
import com.codeit.moim.web.dto.response.Response;
import com.codeit.moim.web.dto.response.comment.ReadMyCommentResponse;
import com.codeit.moim.web.dto.response.comment.ReadMyMeetingCommentResponse;
import com.codeit.moim.web.dto.response.mypage.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.data.domain.Slice;
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

    @Operation(
            summary = "Get logged in user data for banner",
            description = "Get user name, email, profile pic, phone"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get success")
    })
    @GetMapping("/banner")
    public Response<ReadLoggedInUserResponse> getLoggedInUserData(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        int userId = userDetails.getUserId();
        return Response.ok(myPageService.getUserData(userId));
    }


    @Operation(
            summary = "Update user contact",
            description = "Update user contact. Fields can be null"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update success")
    })
    @PutMapping("/contact")
    public Response<UpdateContactResponse> updateUserContact(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateContactRequest request
    ){
        int userId = userDetails.getUserId();
        return Response.ok(myPageService.updateUserContact(userId, request));
    }

    @Operation(
            summary = "Create user skill",
            description = "Create user skill with skill array request. Existing skills will be deleted and be created again."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create success")
    })
    @PostMapping("/skills")
    public Response<CreateUserSkillResponse> createUserSkill(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CreateUserSkillRequest request
    ){
        int userId = userDetails.getUserId();
        return Response.ok(myPageService.createUserSkill(userId, request));
    }

    @Operation(
            summary = "Update user info",
            description = "Update user name, intro, position, gender, age and location. Update fields can be null"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update success")
    })
    @PutMapping("/profile")
    public Response<UpdateUserResponse> updateUserInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateUserRequest request
    ){
        int userId = userDetails.getUserId();
        return Response.ok(myPageService.updateUserInfo(userId, request));
    }

    @Operation(
            summary = "Update user password",
            description = "Check user current password and update encoded password"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update success")
    })
    @PutMapping("/password")
    public Response<UpdatePasswordResponse> updatePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdatePasswordRequest request
    ){
        int userId = userDetails.getUserId();
        return Response.ok(myPageService.updateUserPassword(userId, request));
    }

    @Operation(
            summary = "Get my comments",
            description = "Get my comment, infinite scroll applied with min size 3"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get success")
    })
    @GetMapping("/comments")
    public Response<Slice<ReadMyCommentResponse>> readMyComemnts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute ReadMyCommentRequest request
    ) {
        int userId = userDetails.getUserId();
        return Response.ok(myPageService.getMyComments(userId, request));
    }

    @Operation(
            summary = "Get my profile",
            description = "Get user info, user skills and contact"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get success")
    })
    @GetMapping("/profile")
    public Response<ReadUserResponse> readUserInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        int userId = userDetails.getUserId();
        return Response.ok(myPageService.readUser(userId));
    }

    @Operation(
            summary = "Get meetings to create comments",
            description = "Get my meetings to create comments, infinite scroll applied with min size 3"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get success")
    })
    @GetMapping("/meeting-comment")
    public Response<Slice<ReadMyMeetingCommentResponse>> readMyMeetingForComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute ReadMyMeetingCommentRequest request
    ) {
        int userId = userDetails.getUserId();
        return Response.ok(myPageService.getMyMeetingForComment(userId, request));
    }
}
