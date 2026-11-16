package com.school.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.school.model.DbUser;
import com.school.repository.DbUserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private DbUserRepository dbreUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		DbUser user =  dbreUserRepository.findByUsername(username);
		if(user==null)
			throw new UsernameNotFoundException("User does not exist");
		return new UserDecorator(user);
	}

}
