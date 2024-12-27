//package com.example.trelloproject.global.config;
//
//import com.example.trelloproject.global.filter.LoginFilter;
//import jakarta.servlet.Filter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
//@RequiredArgsConstructor
//public class WebConfig {
//
//	@Bean
//	public FilterRegistrationBean loginFilter(){
//
//		// 필터를 등록하고 설정
//		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
//
//		// HTTP 요청에 대해 로그인 작업 처리, LoginFilter 인스턴스를 필터로 설정
//		filterRegistrationBean.setFilter(new LoginFilter());
//
//		// 모든 URL 패턴에 대해 필터가 동작하도록 설정
//		filterRegistrationBean.addUrlPatterns("/*");
//
//		filterRegistrationBean.setOrder(1);
//
//		// 필터 설정이 완료된 FilterRegistrationBean 객체 반환
//		return filterRegistrationBean;
//	}
//
//}
