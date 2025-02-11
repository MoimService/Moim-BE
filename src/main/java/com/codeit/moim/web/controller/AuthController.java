package com.codeit.moim.web.controller;

import com.codeit.moim.service.user.UserService;
import com.codeit.moim.web.dto.request.auth.LoginRequest;
import com.codeit.moim.web.dto.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auths")
public class AuthController {
    private final UserService userService;
    @Operation(summary = "login", description = "Login API with email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login success")
    })
    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse){
        String token = userService.login(loginRequest);
        httpServletResponse.addHeader("Access-Control-Expose-Headers", "token");
        httpServletResponse.setHeader("token", token);

        return ResponseEntity.ok( "Login success");
    }
}
