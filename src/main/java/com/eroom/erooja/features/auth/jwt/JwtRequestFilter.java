package com.eroom.erooja.features.auth.jwt;

import com.eroom.erooja.domain.model.MemberAuth;
import com.eroom.erooja.features.auth.service.MemberAuthService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

    private final MemberAuthService memberAuthService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        String uid = null;
        String token = null;
        if (authorization != null && authorization.startsWith(AUTHORIZATION_HEADER_PREFIX)) {
            token = authorization.replace(AUTHORIZATION_HEADER_PREFIX, "");

            try {
                uid = jwtTokenProvider.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "올바르지 않은 토큰입니다. - token : " + token);
            } catch (ExpiredJwtException e) {
                response.sendError(HttpServletResponse.SC_GONE, "만료된 토큰, 또는 요청입니다.");
            }
        } else {
            logger.warn(
                    "Authorization 헤더가 없거나, 토큰 형식 포맷 " +
                    "- Authorization : {}[token] 을 지키지 않은 요청 - request {}", AUTHORIZATION_HEADER_PREFIX, request);
        }

        if (uid != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            MemberAuth memberAuth = (MemberAuth) memberAuthService.loadUserByUsername(uid);

            if (jwtTokenProvider.validateToken(token, memberAuth)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(memberAuth, memberAuth.getPassword(), memberAuth.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
