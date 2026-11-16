package com.school.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.school.dto.RegisterUserDto;
import com.school.exception.UserCustomException;
import com.school.model.DbUser;
import com.school.model.Role;
import com.school.repository.DbUserRepository;
import com.school.repository.RoleRepository;

@Service
public class DbUserService {

	@Autowired
	private DbUserRepository dbUserRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

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

}
