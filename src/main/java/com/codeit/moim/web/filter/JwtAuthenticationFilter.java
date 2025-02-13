package com.codeit.moim.web.filter;

import com.codeit.moim.common.config.JwtTokenProvider;
import com.codeit.moim.common.exception.ApplicationException;
import com.codeit.moim.common.exception.jwt.JwtException;
import com.codeit.moim.common.exception.payload.ErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private static final int UNAUTHORIZED = 401;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = jwtTokenProvider.resolveToken(request);

        try{
            if(jwtToken == null){
                throw new JwtException(ErrorStatus.toErrorStatus(
                        "Header is empty. JWT token not found", UNAUTHORIZED
                ));
            }
            if(jwtTokenProvider.validToken(jwtToken)){
                Authentication auth = jwtTokenProvider.getAuthentication(jwtToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }catch(ApplicationException e){
            e.printStackTrace();
            processExceptionHandle(response, e.getErrorStatus());
            return;
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 필터 내부에서 예외처리하는 메소드
     * @param response HttpServletResponse
     * @param errorStatus catch에서 받은 ApplicationException의 ErrorStatus
     */
    private void processExceptionHandle(HttpServletResponse response, ErrorStatus errorStatus) {
        response.setStatus(errorStatus.status());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String json = new ObjectMapper().writeValueAsString(errorStatus);
            response.getWriter().write(json);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    /**
     * 필터링을 하지 않도록 하는 메소드입니다. excludedPaths 배열 안의 엔드포인트는 필터를 하지 않게 됩니다.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludedPaths = {
                "/api/v1/auths/signup/**",
                "/api/v1/auths/login",
                "/v3/**",
                "/swagger-ui/**"
        };
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        log.info("Checking request URI for exclusion: " + request.getRequestURI());

        for (String excludedPath : excludedPaths) {
            if (antPathMatcher.match(excludedPath, request.getRequestURI())) {
                log.info("Excluding path from JWT filter: " +  request.getRequestURI());
                return true;
            }
        }
        return false;
    }
}


