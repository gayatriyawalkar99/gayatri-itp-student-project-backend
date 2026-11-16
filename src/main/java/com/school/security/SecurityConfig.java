package com.school.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authenticationProvider(myAuthenticationProvider());

		http.authorizeHttpRequests(
				auth -> auth.requestMatchers("/saveStudent", "/getAllStudent", "/getSingleStudent/**")
						.hasAnyAuthority("USER", "ADMIN")

						.requestMatchers("/deleteStudentById/**", "/updateStudentById/**", "/updateStudentPhoto/**")
						.hasAuthority("ADMIN")

						.requestMatchers("/registerUser", "/getAllRoles").permitAll()

						.anyRequest().authenticated())

				.formLogin(form -> form.loginProcessingUrl("/login")

						.successHandler((request, response, authentication) -> {
							response.setStatus(200);
							response.setContentType("application/json");

							response.getWriter().write("{\"message\":\"Login successful\"}");
						})

						.failureHandler((request, response, exception) -> {
							response.setStatus(401);
							response.setContentType("application/json");

							response.getWriter().write("{\"message\":\"Invalid username or password\"}");
						})

						.permitAll())

				.logout(logout -> logout.logoutSuccessUrl("/login").permitAll())

				.exceptionHandling(exception -> exception.accessDeniedPage("/getAllStudent/403"))

				.cors(cors -> {
				})

				.csrf(csrf -> csrf.disable());

		return http.build();
	}

	@Bean
	public UserDetailsService mySetUserDetailsService() {
		return new MyUserDetailsService();
	}

	@Bean
	public AuthenticationProvider myAuthenticationProvider() {
		DaoAuthenticationProvider dao = new DaoAuthenticationProvider(mySetUserDetailsService());
		dao.setPasswordEncoder(myPasswordEncoder());
		return dao;
	}

	@Bean
	public PasswordEncoder myPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
