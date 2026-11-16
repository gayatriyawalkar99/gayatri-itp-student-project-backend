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
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

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

						.requestMatchers("/registerUser", "/getAllRoles", "/userLogin").permitAll()

						.anyRequest().authenticated())

				.logout(logout -> logout.logoutSuccessUrl("/login").permitAll())
				.securityContext(context -> context.securityContextRepository(securityContextRepository()))

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

	@Bean
	public SecurityContextRepository securityContextRepository() {
		return new HttpSessionSecurityContextRepository();
	}

}
