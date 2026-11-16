package com.school.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import com.school.dto.LoginDto;
import com.school.dto.RegisterUserDto;
import com.school.exception.UserCustomException;
import com.school.model.DbUser;
import com.school.model.Role;
import com.school.repository.DbUserRepository;
import com.school.repository.RoleRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class DbUserService {

	@Autowired
	private DbUserRepository dbUserRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private SecurityContextRepository securityContextRepository;

	public void registerUser(RegisterUserDto registerUserDto) {
		DbUser user = dbUserRepository.findByUsername(registerUserDto.getUsername());
		if (user != null)
			throw new UserCustomException("User with username : " + registerUserDto.getUsername() + " already exists");

		user = new DbUser();
		user.setUsername(registerUserDto.getUsername());

		// Encode password
		user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));

		List<Role> selectedRoles = roleRepository.findAllById(registerUserDto.getRoles());
		user.setRoles(selectedRoles);

		user.setAccountExpiryDate(LocalDate.now().plusYears(1));
		user.setCredentialsExpiryDate(LocalDate.now().plusMonths(1));
		user.setAccountEnableStatus(1);
		user.setAccountLockedStatus(1);

		dbUserRepository.save(user);
	}

	public List<Role> getAllRoles() {
		return roleRepository.findAll();
	}

	public boolean userLogin(LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {

		// 1. Find user by username
		DbUser user = dbUserRepository.findByUsername(loginDto.getUsername());

		if (user == null) {
			return false;
		}

		// 2. Verify password
		boolean isValid = passwordEncoder.matches(loginDto.getPassword(), user.getPassword());

		if (!isValid) {
			return false;
		}

		// 3. Get user roles
		List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName())).toList();

		// 4. Create Authentication object
		Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), null, authorities);

		// 5. Create SecurityContext
		SecurityContext context = SecurityContextHolder.createEmptyContext();

		context.setAuthentication(authentication);

		SecurityContextHolder.setContext(context);

		// 6. Save authentication in HTTP session
		securityContextRepository.saveContext(context, request, response);

		return true;
	}

}
