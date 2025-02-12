package com.codeit.moim.web.controller;

import com.codeit.moim.service.user.UserService;
import com.codeit.moim.web.dto.request.auth.LoginRequest;
import com.codeit.moim.web.dto.request.auth.SignUpCheckRequest;
import com.codeit.moim.web.dto.request.auth.SignUpRequest;
import com.codeit.moim.web.dto.response.Response;
import com.codeit.moim.web.dto.response.auth.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auths")
public class AuthController {
    private final UserService userService;

    @Operation(summary = "signup", description = "SignUp API. Basic fields will be made with default value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SignUp success")
    })
    @PostMapping(value = "/signup")
    public Response signUp(@RequestBody SignUpRequest signUpRequest){
        return Response.ok( userService.signUpUser(signUpRequest));
    }

    @Operation(summary = "login", description = "Login API with email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login success")
    })
    @PostMapping(value = "/login")
    public Response login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse){
        String token = userService.login(loginRequest);
        httpServletResponse.addHeader("Access-Control-Expose-Headers", "token");
        httpServletResponse.setHeader("token", token);

        return Response.ok(new LoginResponse(loginRequest.email()));
    }

    @Operation(summary = "name check", description = "Check if name already exists in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Name check success")
    })
    @GetMapping(value = "/signup/name")
    public Response nameCheck(@RequestParam String name){
        return Response.ok( userService.userNameCheck(name) );
    }

    @Operation(summary = "email check", description = "Check if email already exists in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email check success")
    })
    @GetMapping(value = "/signup/email" +
            "")
    public Response emailCheck(@RequestParam String email){
        return Response.ok( userService.userEmailCheck(email) );
    }



}
