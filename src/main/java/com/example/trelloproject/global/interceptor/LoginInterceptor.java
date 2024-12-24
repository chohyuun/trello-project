package com.example.trelloproject.global.interceptor;

import com.example.trelloproject.global.constant.Const;
import com.example.trelloproject.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;

		HttpSession session = request.getSession(false);

		if(session == null || session.getAttribute(Const.LOGIN_USER) == null) {
			throw new RuntimeException("로그인 세션이 존재하지 않습니다");
		}

		User user = (User) session.getAttribute(Const.LOGIN_USER);
		
		request.setAttribute(Const.LOGIN_USER, user);

		return true;
	}
}
