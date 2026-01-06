package fikv.ariseth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fikv.ariseth.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByLogin(String login);

	boolean existsByEmail(String email);
}
