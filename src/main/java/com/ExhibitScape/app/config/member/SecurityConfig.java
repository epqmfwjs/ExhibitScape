package com.ExhibitScape.app.config.member;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.ExhibitScape.app.controller.member.CustomAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {

			return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	 public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		http
	    .authorizeHttpRequests((auth) -> auth
	        .requestMatchers("/", "/**", "/scheduleBoard/**", "/exhibitscape/**", "/gallery/", "/error").permitAll()
	        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
	        .requestMatchers("/css/**", "/js/**", "/static/**", "/image", "/resources").permitAll()
	        .requestMatchers("/admin").hasRole("ADMIN")
	        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
	        .anyRequest().permitAll()
	    );
//	    .anonymous((anon) -> anon.principal("guest").authorities("ROLE_ANONYMOUS"));
        //.requestMatchers(HttpMethod.GET, "/resources/**").permitAll()
        
        
       http
        		.formLogin((auth) -> auth.loginPage("/login")
        			.loginProcessingUrl("/loginProc").defaultSuccessUrl("/scheduleBoard/list")
        			.usernameParameter("memberId").permitAll()
        		 );	

//		.formLogin((auth) -> auth.loginPage("/login")
//			    .loginProcessingUrl("/loginProc")
//			    .usernameParameter("memberId")
//			    .successHandler(new CustomAuthenticationSuccessHandler())
//			    .permitAll()
//			);
		
        http
        		.csrf((auth) -> auth.disable());

        //다중 로그인 설정 1개만 허용, 새로운 로그인 차단
        http
        		.sessionManagement((auth) -> auth
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true));

        //세션 고정 보호
        http
        		.sessionManagement((auth) -> auth
                .sessionFixation().changeSessionId());
        
        //로그아웃 get방식 설정
//        http
//        		.logout((auth) -> auth.logoutUrl("/logout")
//                .logoutSuccessUrl("/").permitAll());        
//        
        	
        	
    	 http.logout(auth -> auth
                 .logoutUrl("/logout")
                 .logoutSuccessUrl("/")
                 .logoutSuccessHandler((request, response, authentication) ->
                         response.sendRedirect("/scheduleBoard/list"))
         );
    	 
    	 return http.build();
    	 
    	 
}
	
//	 public void configure(WebSecurity web) throws Exception {
//	        web
//	        .ignoring()
//	        .requestMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**","/vendor/**","/fonts/**");
//	    }
}
