package com.school.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.dto.LoginDto;
import com.school.dto.RegisterUserDto;
import com.school.model.Role;
import com.school.service.DbUserService;

import jakarta.validation.Valid;

@RestController
public class DbUserController {

	@Autowired
	private DbUserService dbUserService;

	@PostMapping("/registerUser")
	public ResponseEntity<String> registerUser(@RequestBody @Valid RegisterUserDto registerUserDto) {
		dbUserService.registerUser(registerUserDto);
		return new ResponseEntity<String>("User Registered Successfully", HttpStatus.OK);
	}

	@GetMapping("/getAllRoles")
	public ResponseEntity<List<Role>> getAllRoles() {
		return new ResponseEntity<List<Role>>(dbUserService.getAllRoles(), HttpStatus.OK);
	}
	
	@PostMapping("/userLogin")
	public ResponseEntity<String> userLogin(@RequestBody @Valid LoginDto loginDto) {
		boolean isValid = dbUserService.userLogin(loginDto);

		if (isValid)
			return new ResponseEntity<String>("Logged in Successfully", HttpStatus.OK);

		return new ResponseEntity<String>("Invalid Username or Password", HttpStatus.UNAUTHORIZED);
	}

}
