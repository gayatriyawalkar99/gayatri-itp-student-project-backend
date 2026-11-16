package com.school.security;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.school.model.DbUser;
import com.school.model.Role;

public class UserDecorator implements UserDetails {

	private DbUser user;

	public UserDecorator(DbUser user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<Role> roles = user.getRoles();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}

		return authorities;

	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		LocalDate accExpiryDate = user.getAccountExpiryDate();
		LocalDate todaysDate = LocalDate.now();
		if (todaysDate.isAfter(accExpiryDate))
			return false;
		else
			return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		int accLockStatus = user.getAccountLockedStatus();
		if (accLockStatus == 1)
			return true;
		else
			return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		LocalDate credExpiryDate = user.getCredentialsExpiryDate();
		LocalDate todaysDate = LocalDate.now();
		if (todaysDate.isAfter(credExpiryDate))
			return false;
		else
			return true;
	}

	@Override
	public boolean isEnabled() {
		int accEnableStatus = user.getAccountEnableStatus();
		if (accEnableStatus == 1)
			return true;
		else
			return false;
	}

}
