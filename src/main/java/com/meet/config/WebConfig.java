package com.meet.config;

import java.util.List;

import com.meet.interceptor.UserLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	@Autowired
	UserArgumentResolver userArgumentResolver;
	@Autowired
	AdminArgumentResolver adminArgumentResolver;
	@Autowired
	UserLoginInterceptor userLoginInterceptor;
//	@Autowired
//	AccessInterceptor accessInterceptor;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(userArgumentResolver);
		argumentResolvers.add(adminArgumentResolver);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(accessInterceptor);

		registry.addInterceptor(userLoginInterceptor)
				.addPathPatterns("/profile")
				.addPathPatterns("/password")
				.addPathPatterns("/bought")
				.addPathPatterns("/review")
				.addPathPatterns("/user/upload")
				.addPathPatterns("/user/password")
				.addPathPatterns("/user/profile")
				.addPathPatterns("/appoint/mine")
				.addPathPatterns("/review");
	}

}
