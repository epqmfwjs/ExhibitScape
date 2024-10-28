package com.ExhibitScape.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ExhibitScape.app.controller.member.LoginPageInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	private String resourcePath = "/upload/**"; // view 에서 접근할 경로
	//private String savePath = "file:///C:/imgs/"; // 실제 파일 저장 경로(win)
	private String savePath = "file:///imgs/"; // 실제 파일 저장 경로(리눅스)
    
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcePath)
                .addResourceLocations(savePath);
    }
	
	
	@Autowired
    private LoginPageInterceptor loginPageInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginPageInterceptor());
    }
    
   
}
