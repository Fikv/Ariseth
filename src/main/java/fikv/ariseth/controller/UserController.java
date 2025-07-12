package fikv.ariseth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.stream.Collectors;
import java.util.Map;

@RestController
public class UserController {

	@GetMapping("/api/me")
	public ResponseEntity<Map<String, Object>> me(Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			return ResponseEntity.ok(Map.of("success", false));
		}
		return ResponseEntity.ok(Map.of(
				"success", true,
				"username", authentication.getName(),
				"roles", authentication.getAuthorities().stream()
									   .map(Object::toString)
									   .collect(Collectors.toList())
		));
	}
}
