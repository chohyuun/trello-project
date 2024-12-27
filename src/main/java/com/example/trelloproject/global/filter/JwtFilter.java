package com.example.trelloproject.global.filter;

import com.example.trelloproject.global.jwt.JwtUtil;
import com.example.trelloproject.user.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {


	private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserDetailService userDetailService;

//	@Override
//	protected void doFilterInternal(
//		HttpServletRequest request,
//		HttpServletResponse response,
//		FilterChain chain
//	) throws ServletException, IOException {
//		String authorizationHeader = request.getHeader("Authorization");
//
//		String username = null;
//		String token = null;
//
//		// JWT 헤더에서 추출
//		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//			token = authorizationHeader.substring(7);
//			try {
//				username = jwtUtil.extractEmail(token); // JWT에서 사용자 이름 추출
//			} catch (Exception e) {
//				logger.warn("Invalid JWT token: {}", e.getMessage());
//			}
//		}
//
//		// 사용자 이름이 있고 SecurityContext에 인증 정보가 없는 경우
//		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//			// 데이터베이스에서 사용자 정보 로드
//			UserDetails userDetails = userDetailService.loadUserByUsername(username);
//
//			// JWT가 유효한지 확인
//			if (jwtUtil.validateToken(token, userDetails.getUsername())) {
//				// 인증 정보 생성 및 SecurityContext에 설정
//				UsernamePasswordAuthenticationToken authToken =
//					new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//				SecurityContextHolder.getContext().setAuthentication(authToken);
//			}
//		}
//
//		// 다음 필터로 요청 전달
//		chain.doFilter(request, response);
//	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain chain
	) throws ServletException, IOException {
		String authorizationHeader = request.getHeader("Authorization");

		String username = null;
		String token = null;

		// JWT 헤더에서 추출
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			token = authorizationHeader.substring(7);
			try {
				username = jwtUtil.extractEmail(token); // JWT에서 사용자 이름 추출
				logger.info("Extracted username from token: {}", username);  // JWT 토큰에서 추출한 사용자 이름 로그
			} catch (Exception e) {
				logger.warn("Invalid JWT token: {}", e.getMessage());
			}
		}

		// 사용자 이름이 있고 SecurityContext에 인증 정보가 없는 경우
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// 데이터베이스에서 사용자 정보 로드
			UserDetails userDetails = userDetailService.loadUserByUsername(username);
			logger.info("UserDetails loaded for: {}", username);

			// JWT가 유효한지 확인
			if (jwtUtil.validateToken(token, userDetails.getUsername())) {
				logger.info("JWT token is valid for user: {}", username);
				// 인증 정보 생성 및 SecurityContext에 설정
				UsernamePasswordAuthenticationToken authToken =
					new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authToken);
			} else {
				logger.warn("Invalid JWT token for user: {}", username);
			}
		}

		// 다음 필터로 요청 전달
		chain.doFilter(request, response);
	}

}
