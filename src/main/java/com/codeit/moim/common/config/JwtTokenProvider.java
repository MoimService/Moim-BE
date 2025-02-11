package com.codeit.moim.common.config;

import com.codeit.moim.common.exception.jwt.JwtException;
import com.codeit.moim.common.exception.payload.ErrorStatus;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final int UNAUTHORIZED = 401;
    private static final long TOKEN_VALID_MILLI_SECONDS =1000L*60*60*24; //24h


    @Value("${jwtpassword.source}")
    private String secretKeySource;
    private String secretKey;

    @PostConstruct
    public void setUp(){
        secretKey = Base64.getEncoder()
                .encodeToString(secretKeySource.getBytes());
    }

    private final UserDetailsService userDetailsService;

    public String createToken(String email){
        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+ TOKEN_VALID_MILLI_SECONDS))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    public String resolveToken(HttpServletRequest request){
        String token = request.getHeader("token");
        return token;
    }

    public boolean validToken(String jwtToken){
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwtToken)
                    .getBody();
            Date now = new Date();
            return claims.getExpiration().after(now);
        } catch (ExpiredJwtException e) {
            throw new JwtException(ErrorStatus.toErrorStatus("JWT token expired", UNAUTHORIZED));
        } catch (SignatureException e) {
            throw new JwtException(ErrorStatus.toErrorStatus("JWT token secret key is not valid.", UNAUTHORIZED));
        } catch (MalformedJwtException e) {
            throw new JwtException(ErrorStatus.toErrorStatus("Malformed JWT token format", UNAUTHORIZED));
        } catch (UnsupportedJwtException e) {
            throw new JwtException(ErrorStatus.toErrorStatus("This JWT format is not supported", UNAUTHORIZED));
        } catch (Exception e) {
            throw new JwtException(ErrorStatus.toErrorStatus("JWT token is not valid", UNAUTHORIZED));
        }
    }

    public Authentication getAuthentication(String jwtToken){
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserEmail(jwtToken));
        return new UsernamePasswordAuthenticationToken(userDetails, " ", userDetails.getAuthorities());
    }

    public String getUserEmail(String jwtToken) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtToken)
                .getBody()
                .getSubject();
    }



}
