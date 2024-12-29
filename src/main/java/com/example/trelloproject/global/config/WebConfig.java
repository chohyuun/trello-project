
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
package com.example.trelloproject.global.config;

import com.example.trelloproject.card.MultipartJackson2HttpMessageConverter;
import com.example.trelloproject.global.interceptor.LoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
	private  LoginInterceptor loginInterceptor;
	private  MultipartJackson2HttpMessageConverter multipartJackson2HttpMessageConverter;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginInterceptor);
	}



	//카드 파일 관련
	@Bean
	public MultipartResolver multipartResolver() {
		StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
		return resolver;
	}
	public WebConfig(MultipartJackson2HttpMessageConverter multipartJackson2HttpMessageConverter) {
		this.multipartJackson2HttpMessageConverter = multipartJackson2HttpMessageConverter;
	}
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(multipartJackson2HttpMessageConverter);
	}
}

