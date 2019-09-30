package org.atwjsw.security.server.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// mock a UserDetails. any username received will have a matched
		// UserDetails with a password of "123456" and ROLE_ADMIN
		// In real application, following logic will likely be implemented
		// through database access.
		return User.withUsername(username)
				.password(passwordEncoder.encode("123456"))
				.authorities("ROLE_ADMIN")
				.build();
	}

}
