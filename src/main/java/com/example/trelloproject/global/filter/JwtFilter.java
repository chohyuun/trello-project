package com.example.trelloproject.global.filter;

import com.example.trelloproject.global.exception.BusinessException;
import com.example.trelloproject.global.exception.ExceptionType;
import com.example.trelloproject.global.jwt.JwtUtil;
import com.example.trelloproject.user.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserDetailService userDetailService;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain chain
	) throws ServletException, IOException {
		String authorizationHeader = request.getHeader("Authorization");

		String username = null;
		String token = null;

		// JWT 토큰 추출
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			token = authorizationHeader.substring(7); // "Bearer " 이후의 토큰만 추출
			try {
				username = jwtUtil.extractEmail(token); // JWT에서 사용자 이메일 추출
			} catch (Exception e) {
				log.warn("Invalid JWT token: {}", e.getMessage());
				throw new BusinessException(ExceptionType.INVALID_TOKEN);
			}
		}

		// 사용자 이름이 있고 SecurityContext에 인증 정보가 없는 경우
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			try {
				// 데이터베이스에서 사용자 정보 로드
				UserDetails userDetails = userDetailService.loadUserByUsername(username);

				// JWT 토큰 유효성 검사
				if (jwtUtil.validateToken(token, userDetails.getUsername())) {

					// 사용자 권한 설정 (Role 접두어 'ROLE_' 추가)
					UsernamePasswordAuthenticationToken authToken =
						new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authToken);
				} else {
					throw new BusinessException(ExceptionType.INVALID_TOKEN);
				}
			} catch (Exception e) {
				log.error("Error occurred while loading user details or validating token: {}", e.getMessage());
				throw new BusinessException(ExceptionType.INVALID_TOKEN);
			}
		}

		// 다음 필터로 요청 전달
		chain.doFilter(request, response);
	}
}
