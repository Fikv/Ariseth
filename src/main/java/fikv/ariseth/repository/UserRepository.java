package fikv.ariseth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fikv.ariseth.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByUsername(String username);
}