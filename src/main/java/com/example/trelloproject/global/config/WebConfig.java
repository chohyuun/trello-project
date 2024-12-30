package com.example.trelloproject.global.config;

import com.example.trelloproject.card.MultipartJackson2HttpMessageConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
	private  MultipartJackson2HttpMessageConverter multipartJackson2HttpMessageConverter;

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

