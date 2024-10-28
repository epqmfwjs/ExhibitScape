package com.ExhibitScape.app.config.community;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Appconfig implements WebMvcConfigurer{
	
/*	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry.addResourceHandler("/comFile/**")
			.addResourceLocations("file:///C:/Img/upload/");
	}*/
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("/comFile/**")
			.addResourceLocations("file:///Img/upload/");
	}

}
