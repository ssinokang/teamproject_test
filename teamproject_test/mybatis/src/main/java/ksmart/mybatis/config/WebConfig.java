package ksmart.mybatis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import ksmart.mybatis.interceptor.CommonInterceptor;
import ksmart.mybatis.interceptor.LoginInterceptor;


/**
 * mvc 추가하는 설정들 넣는 것
 * 인터셉터 등
 * @author ksmart
 */
@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	private final CommonInterceptor commonInterceptor;
	private final LoginInterceptor loginInterceptor;
	
	public WebConfig(CommonInterceptor commonInterceptor, LoginInterceptor loginInterceptor) {
		this.commonInterceptor = commonInterceptor;
		this.loginInterceptor = loginInterceptor;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		/**
		 * 몇개의 어디든지의 경로에 추가하고 싶으면 addPathPatterns("/**")을 사용한다.
		 * 특정 경로를 제외하고 싶으면 excludePathPatterns("/*")를 사용
		 * favicon - 브라우저 탭 아이콘
		 */
		registry.addInterceptor(commonInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns("/css/**")
				.excludePathPatterns("/js/**")
				.excludePathPatterns("/favicon.ico");
		/*
		registry.addInterceptor(loginInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns("/")
				.excludePathPatterns("/css/**")
				.excludePathPatterns("/js/**")
				.excludePathPatterns("/favicon.ico")
				.excludePathPatterns("/member/addMember")
				.excludePathPatterns("/member/idCheck")
				.excludePathPatterns("/login")
				.excludePathPatterns("/logout");
		*/
		WebMvcConfigurer.super.addInterceptors(registry);
	}

}
