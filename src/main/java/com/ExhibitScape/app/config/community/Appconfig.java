package com.ExhibitScape.app.config.community;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
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

	// 아이피 노출방지를 위해 .env 파일 사용
	private final Dotenv dotenv = Dotenv.configure().load();  // .env 파일 로드

	@Bean
	public String socketUrl() {
		return dotenv.get("SOCKET_URL");
	}

}
