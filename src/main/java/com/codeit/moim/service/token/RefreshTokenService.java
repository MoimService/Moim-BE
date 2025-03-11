package com.codeit.moim.service.token;

import com.codeit.moim.domain.RefreshToken;
import com.codeit.moim.web.dto.request.token.TokenRefreshRequest;
import com.codeit.moim.web.dto.response.token.JwtResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String email);

    RefreshToken verifyExpiration(RefreshToken token, HttpServletRequest httpServletRequest);

    Optional<RefreshToken> findByToken(String requestRefreshToken);

    String refreshToken(TokenRefreshRequest request, HttpServletRequest httpServletRequest);
    //String refreshToken(String refreshToken);

}
