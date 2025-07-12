package fikv.ariseth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import fikv.ariseth.entity.UserEntity;
import fikv.ariseth.repository.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	public JpaUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByUsername(username)
										.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));


		List<GrantedAuthority> authorities = new ArrayList<>(user.getAuthorities());

		return org.springframework.security.core.userdetails.User
				.withUsername(user.getUsername())
				.password(user.getPassword())
				.authorities(authorities)
				.accountExpired(false)
				.accountLocked(false)
				.credentialsExpired(false)
				.disabled(!user.isEnabled())
				.build();
	}

}