package fikv.ariseth.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

public class WebSecurityConfig {

	@Configuration
	@EnableWebSecurity
	public class WebSecurityConfig  {

		@Bean
		protected UserDetailsService userDetailsService() {
			UserDetails user = User.builder()
								   .username("user")
								   .password(passwordEncoder().encode("user123"))
								   .roles("USER")
								   .build();
			UserDetails admin = User.builder()
									.username("admin")
									.password(passwordEncoder().encode("admin123"))
									.roles("USER", "ADMIN")
									.build();
			return new InMemoryUserDetailsManager(user, admin);
		}

		@Bean
		protected PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}

		@Bean
		protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			return http
					.csrf(AbstractHttpConfigurer::disable)
					.authorizeHttpRequests(
							request -> request.requestMatchers("/login").permitAll()
											  .requestMatchers("/**").authenticated()
					)
					.formLogin(Customizer.withDefaults())
					.logout(config -> config
							.logoutUrl("/logout")
							.logoutSuccessUrl("/login"))
					.build();
		}
	}

}
