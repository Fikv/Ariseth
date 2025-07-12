package fikv.ariseth.entity;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "authorities")
public class Authority implements GrantedAuthority {
	@Id
	@GeneratedValue
	private Long id;
	private String authority;

	@Override
	public String getAuthority() {
		return authority;
	}
}