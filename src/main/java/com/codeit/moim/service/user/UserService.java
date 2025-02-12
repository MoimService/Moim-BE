package com.codeit.moim.service.user;

import com.codeit.moim.web.dto.request.auth.LoginRequest;
import com.codeit.moim.web.dto.request.auth.SignUpCheckRequest;
import com.codeit.moim.web.dto.request.auth.SignUpRequest;
import com.codeit.moim.web.dto.response.auth.SignUpCheckResponse;
import com.codeit.moim.web.dto.response.auth.SignUpResponse;

public interface UserService {
    String login(LoginRequest loginRequest);

    SignUpResponse signUpUser(SignUpRequest signUpRequest);

    SignUpCheckResponse userNameCheck(String name);

    SignUpCheckResponse userEmailCheck(String email);
}
