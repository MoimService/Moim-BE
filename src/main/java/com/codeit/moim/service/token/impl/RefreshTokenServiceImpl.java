package com.codeit.moim.service.token.impl;

import com.codeit.moim.common.config.JwtTokenProvider;
import com.codeit.moim.common.exception.auth.UserNotFoundException;
import com.codeit.moim.common.exception.jwt.TokenRefreshException;
import com.codeit.moim.domain.RefreshToken;
import com.codeit.moim.domain.User;
import com.codeit.moim.repository.RefreshTokenRepository;
import com.codeit.moim.repository.UserRepository;
import com.codeit.moim.service.token.RefreshTokenService;
import com.codeit.moim.web.dto.request.token.TokenRefreshRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private static final long REFRESH_TOKEN_VALID_MILLI_SECONDS = 1000L*60*5; //5mins //1000L*60*60*24; //24h


    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

//    @Override
//    public String refreshToken(String refreshToken) {
//        RefreshToken verifiedToken = findByToken(refreshToken)
//                .map(token -> verifyExpiration(token))
//                .orElseThrow(()-> new TokenRefreshException("Refresh token is not in database."));
//        String accessToken = jwtTokenProvider.createToken(verifiedToken.getUser().getEmail());
//
//        return accessToken;
//    }

    @Override
    public String refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.refreshToken();
        RefreshToken verifiedToken = findByToken(requestRefreshToken)
                .map(token -> verifyExpiration(token))
                .orElseThrow(()-> new TokenRefreshException("Refresh token is not in database."));
        String accessToken = jwtTokenProvider.createToken(verifiedToken.getUser().getEmail());

        return accessToken;
    }




    @Transactional
    @Override
    public RefreshToken createRefreshToken(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UserNotFoundException(email));
        String token = UUID.randomUUID().toString();
        Instant instant = Instant.now().plusMillis(REFRESH_TOKEN_VALID_MILLI_SECONDS);
        if(refreshTokenRepository.existsByUser(user)){
            RefreshToken refreshToken = refreshTokenRepository.findByUser(user);
            refreshToken.updateToken(token, instant);
            RefreshToken newRefreshToken = refreshTokenRepository.save(refreshToken);
            return newRefreshToken;
        }else {
            RefreshToken refreshToken = RefreshToken.toEntity(token, instant, user);
            refreshTokenRepository.save(refreshToken);
            return refreshToken;
        }
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("Refresh token expired. Please make a new login request. Refresh token expired at: " + token.getExpiryDate());
        }
        return token;
    }



}
