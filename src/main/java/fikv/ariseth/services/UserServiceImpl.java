package fikv.ariseth.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fikv.ariseth.entities.User;
import fikv.ariseth.repositories.UserRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User create(User user) {
		return userRepository.save(user);
	}

	@Override
	@Transactional
	public User getById(Long id) {
		return userRepository.findById(id)
							 .orElseThrow(() -> new RuntimeException("User not found"));
	}

	@Override
	@Transactional
	public List<User> getAll() {
		return userRepository.findAll();
	}

	@Override
	public User update(Long id, User user) {
		User existing = getById(id);

		existing.setLogin(user.getLogin());
		existing.setPassword(user.getPassword());
		existing.setEmail(user.getEmail());
		existing.setCreatedTimestamp(user.getCreatedTimestamp());

		return userRepository.save(existing);
	}

	@Override
	public void delete(Long id) {
		userRepository.deleteById(id);
	}
}