package com.codeit.moim.service.user;

import com.codeit.moim.web.dto.request.auth.LoginRequest;

public interface UserService {
    String login(LoginRequest loginRequest);
}
