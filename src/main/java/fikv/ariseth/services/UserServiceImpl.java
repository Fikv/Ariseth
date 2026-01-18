package fikv.ariseth.services;

import java.util.List;

import fikv.ariseth.entities.UserDetailsImpl;
import fikv.ariseth.mappers.UserMapper;
import fikv.ariseth.dtos.UserRequestDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
	private final ApplicationContext applicationContext;
	private final JWTService jwtService;

	/** TODO
	 * CHANGE PROPERTY TO ENV, USE 1 INSTANCE OF BCRYPT
	 */
	BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder(14);

	public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, ApplicationContext applicationContext, JWTService jwtService) {
		this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.applicationContext = applicationContext;
        this.jwtService = jwtService;
    }

	@Override
	public User create(UserRequestDTO user) {
		UserRequestDTO userAfterEncoding = new UserRequestDTO(user.login(),
				bCrypt.encode(user.password()), user.email());

		return userRepository.save(userMapper.toEntity(userAfterEncoding));
	}

	@Override
	@Transactional
	public User getById(Long id) {
		return userRepository.findById(id)
							 .orElseThrow(() -> new RuntimeException(USERNAME_HAS_BEEN_NOT_FOUND));
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
	public String verify(UserRequestDTO userRequestDTO) {
		Authentication authentication = applicationContext.getBean(AuthenticationManager.class)
				.authenticate(new UsernamePasswordAuthenticationToken(
						userRequestDTO.login(), userRequestDTO.password()));

		if(authentication.isAuthenticated()) {
			return jwtService.generateToken(userRequestDTO.login());
		}

		/** TODO
		 * FIX
		 */

		return "no";
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		return userMapper.toUserDetail(userRepository
				.findByLogin(username).orElseThrow(()
						-> new UsernameNotFoundException(USERNAME_HAS_BEEN_NOT_FOUND)));
	}
}