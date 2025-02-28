package com.codeit.moim.service.token;

import com.codeit.moim.domain.RefreshToken;
import com.codeit.moim.web.dto.request.token.TokenRefreshRequest;
import com.codeit.moim.web.dto.response.token.JwtResponse;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String email);

    RefreshToken verifyExpiration(RefreshToken token);

    Optional<RefreshToken> findByToken(String requestRefreshToken);

    JwtResponse refreshToken(TokenRefreshRequest request);

}
