package com.in28minutes.rest.webservices.restfulwebservices.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
//import static org.springframework.security.config.Customizer.withDefault;
//import org.springframework.security.config;

@Configuration
public class SpringSecurityConfiguration {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// authenticate all request 
		http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
		
		http.httpBasic(Customizer.withDefaults());
		
		http.csrf().disable();
		return http.build();
	}
}
