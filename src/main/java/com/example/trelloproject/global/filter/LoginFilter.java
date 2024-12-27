//package com.example.trelloproject.global.filter;
//
//import com.example.trelloproject.user.User;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import java.io.IOException;
//import jakarta.servlet.Filter;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.util.PatternMatchUtils;
//
//@Slf4j
//public class LoginFilter implements Filter {
//
//	// 화이트 리스트 : 로그인 없이 접근 가능한 URL 목록
//	private static final String[] WHITE_LIST = {"/","/users/signup","/login"};
//
//	/**
//	 * 필터링 작업 수행
//	 *
//	 * @param servletRequest  클라이언트 요청
//	 * @param servletResponse 서버 응답
//	 * @param filterChain     다음 필터로의 전달 체인
//	 * @throws IOException      입출력 예외 처리
//	 * @throws ServletException 서블릿 예외 처리
//	 */
//	@Override
//	public void doFilter(
//		ServletRequest servletRequest,
//		ServletResponse servletResponse,
//		FilterChain filterChain
//	) throws IOException, ServletException {
//
//		// HttpServletRequest로 형변환하여 HTTP 요청 정보 사용
//		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
//		String requestURL = httpServletRequest.getRequestURI(); // 요청된 URL 추출
//
//		// HttpServletResponse로 형변환하여 HTTP 응답 제어
//		HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
//
//		// 요청 URL이 화이트리스트에 포함되지 않은 경우 처리
//		if (!isWhiteList(requestURL)) {
//			// 세션을 가져옴. 세션이 존재하지 않으면 null 반환
//			HttpSession session = httpServletRequest.getSession(false);
//
//			// 세션이 없거나 로그인 정보가 없는 경우
//			if (session == null || session.getAttribute("LOGIN_USER") == null) {
//				log.warn("Unauthorized access attempt to {}", requestURL); // 로그 경고 출력
//				httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 설정
//				httpServletResponse.setContentType("text/plain;charset=UTF-8");
//				httpServletResponse.getWriter().write("로그인 해주세요"); // 클라이언트에 메시지 전송
//				return;
//			}
//
//			// 세션에 저장된 객체가 LoginResponseDto인지 확인
//			User loginUser = (User) session.getAttribute("LOGIN_USER");
//			if (loginUser == null) {
//				log.warn("Invalid session data for {}", requestURL); // 잘못된 세션 데이터 로그 출력
//				httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401 상태 설정
//				httpServletResponse.setContentType("text/plain;charset=UTF-8");
//				httpServletResponse.getWriter().write("로그인 해주세요"); // 클라이언트에 메시지 전송
//				return;
//			}
//		}
//		// 필터 체인에 현재 요청과 응답 전달
//		filterChain.doFilter(servletRequest, servletResponse);
//	}
//
//	/**
//	 * 요청 URL이 화이트리스트에 포함되어 있는지 확인
//	 *
//	 * @param requestURL 요청된 URL
//	 * @return 화이트리스트에 포함되어 있으면 true, 아니면 false
//	 */
//	private boolean isWhiteList(String requestURL) {
//		// 화이트리스트와 요청 URL을 단순 매칭
//		return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURL);
//	}
//}