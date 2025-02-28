package com.codeit.moim.web.controller;

import com.codeit.moim.common.config.JwtTokenProvider;
import com.codeit.moim.common.exception.jwt.TokenRefreshException;
import com.codeit.moim.common.exception.payload.ErrorStatus;
import com.codeit.moim.domain.RefreshToken;
import com.codeit.moim.domain.User;
import com.codeit.moim.service.token.RefreshTokenService;
import com.codeit.moim.service.user.UserService;
import com.codeit.moim.web.dto.request.auth.LoginRequest;
import com.codeit.moim.web.dto.request.auth.SignUpCheckRequest;
import com.codeit.moim.web.dto.request.auth.SignUpRequest;
import com.codeit.moim.web.dto.request.token.TokenRefreshRequest;
import com.codeit.moim.web.dto.response.Response;
import com.codeit.moim.web.dto.response.auth.LoginResponse;
import com.codeit.moim.web.dto.response.auth.SignUpCheckResponse;
import com.codeit.moim.web.dto.response.auth.SignUpResponse;
import com.codeit.moim.web.dto.response.token.JwtResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auths")
public class AuthController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;



    @Operation(summary = "signup", description = "SignUp API. Basic fields will be made with default value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SignUp success")
    })
    @PostMapping(value = "/signup")
    public Response<SignUpResponse> signUp(
            @Valid @RequestBody SignUpRequest signUpRequest){
        return Response.ok( userService.signUpUser(signUpRequest));
    }

    @Transactional
    @Operation(summary = "login", description = "Login API with email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login success")
    })
    @PostMapping(value = "/login")
    public Response<LoginResponse> login(
            @Valid @RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse){
        String accessToken = userService.login(loginRequest);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginRequest.email());
        httpServletResponse.addHeader("Access-Control-Expose-Headers", "token");
        httpServletResponse.setHeader("token", accessToken);
        return Response.ok(new LoginResponse(loginRequest.email(), accessToken, refreshToken.getToken()));
    }

    @PostMapping(value = "/refresh")
    public Response<JwtResponse> refreshToken(
            @Valid @RequestBody TokenRefreshRequest request
    ){
    return Response.ok(refreshTokenService.refreshToken(request));
    }


    @Operation(summary = "name check", description = "Check if name already exists in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Name check success")
    })
    @GetMapping(value = "/signup/name")
    public Response<SignUpCheckResponse> nameCheck(@RequestParam String name){
        return Response.ok( userService.userNameCheck(name));
    }

    @Operation(summary = "email check", description = "Check if email already exists in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email check success")
    })
    @GetMapping(value = "/signup/email")
    public Response<SignUpCheckResponse> emailCheck(@RequestParam String email){
        return Response.ok( userService.userEmailCheck(email));
    }



}
