package com.codeit.moim.common.config;

import com.codeit.moim.common.exception.jwt.JwtNotValidException;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
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
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final long TOKEN_VALID_MILLI_SECONDS = 1000L*60*3; //3min //1000L*60*60*24; //24h


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

    public Cookie createCookie(String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        return cookie;
    }


    public String resolveToken(HttpServletRequest request){
//        String token = request.getHeader("token");
//        return token;

        String token = null;

        if(request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                if(cookie.getName().equals("access_token")) {
                    token = cookie.getValue();
                }
            }
        }
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
            throw new JwtNotValidException("JWT token expired");
        } catch (SignatureException e) {
            throw new JwtNotValidException("JWT token secret key is not valid.");
        } catch (MalformedJwtException e) {
            throw new JwtNotValidException("Malformed JWT token format");
        } catch (UnsupportedJwtException e) {
            throw new JwtNotValidException("This JWT format is not supported");
        } catch (Exception e) {
            throw new JwtNotValidException("JWT token is not valid");
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

    private final Set<String> tokenBlackList = new HashSet<>();
    public void addToBlackList(String accessToken) {
        tokenBlackList.add(accessToken);
    }

    public boolean isTokenBlackListed(String jwtToken){
        return tokenBlackList.contains(jwtToken);
    }




}
