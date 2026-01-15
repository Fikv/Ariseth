package fikv.ariseth.services;

import java.util.List;

import fikv.ariseth.mappers.UserMapper;
import fikv.ariseth.dtos.UserRequestDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fikv.ariseth.entities.User;
import fikv.ariseth.repositories.UserRepository;

import static fikv.ariseth.Util.Constants.Messages.USERNAME_HAS_BEEN_NOT_FOUND;

@Service
@Transactional
@Qualifier("userService")
public class UserServiceImpl implements UserService, UserDetailsService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
		this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

	@Override
	public User create(UserRequestDTO user) {
		return userRepository.save(userMapper.toEntity(user));
	}

	@Override
	@Transactional
	public User getById(Long id) {
		return userRepository.findById(id)
							 .orElseThrow(() -> new RuntimeException("User not found"));
	}

	@Override
	@Transactional(readOnly = true)
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

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		return userMapper.toUserDetail(userRepository
				.findByLogin(username).orElseThrow(()
						-> new UsernameNotFoundException(USERNAME_HAS_BEEN_NOT_FOUND)));
	}
}