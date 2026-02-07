package fikv.ariseth.services;

import fikv.ariseth.dtos.LoginResponse;
import fikv.ariseth.dtos.UserRequestDTO;
import fikv.ariseth.entities.User;
import fikv.ariseth.entities.UserDetailsImpl;
import fikv.ariseth.mappers.UserMapper;
import fikv.ariseth.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static fikv.ariseth.Util.Constants.Messages.USERNAME_HAS_BEEN_NOT_FOUND;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private JWTService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequestDTO userRequestDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setLogin("john");
        user.setPassword("encodedPassword");
        user.setEmail("john@test.com");

        userRequestDTO = new UserRequestDTO(
                "john",
                "password",
                "john@test.com"
        );
    }

    @Test
    void create_shouldEncodePasswordAndSaveUser() {
        User mappedUser = new User();
        mappedUser.setLogin("john");

        when(userMapper.toEntity(any(UserRequestDTO.class))).thenReturn(mappedUser);
        when(userRepository.save(mappedUser)).thenReturn(mappedUser);

        User result = userService.create(userRequestDTO);

        assertThat(result).isNotNull();
        verify(userMapper).toEntity(argThat(dto ->
                dto.login().equals("john") &&
                        !dto.password().equals("password") // encoded
        ));
        verify(userRepository).save(mappedUser);
    }

    @Test
    void getById_shouldReturnUser_whenExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getById(1L);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void getById_shouldThrowException_whenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(USERNAME_HAS_BEEN_NOT_FOUND);
    }

    @Test
    void getAll_shouldReturnListOfUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.getAll();

        assertThat(result).hasSize(1);
    }

    @Test
    void update_shouldUpdateExistingUser() {
        User updated = new User();
        updated.setLogin("newLogin");
        updated.setPassword("newPass");
        updated.setEmail("new@test.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.update(1L, updated);

        assertThat(result.getLogin()).isEqualTo("newLogin");
        assertThat(result.getEmail()).isEqualTo("new@test.com");
        verify(userRepository).save(user);
    }

    @Test
    void delete_shouldCallRepositoryDelete() {
        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void verify_shouldReturnJwtToken_whenAuthenticated() {
        when(applicationContext.getBean(AuthenticationManager.class))
                .thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken("john")).thenReturn("jwt-token");

        fikv.ariseth.dtos.LoginResponse loginResponse = userService.verify(userRequestDTO);

        assertThat(loginResponse.token()).isEqualTo("jwt-token");
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails() {
        when(userRepository.findByLogin("john")).thenReturn(Optional.of(user));
        when(userMapper.toUserDetail(user)).thenReturn((UserDetailsImpl) mock(UserDetails.class));

        assertThatCode(() -> userService.loadUserByUsername("john"))
                .doesNotThrowAnyException();
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenNotFound() {
        when(userRepository.findByLogin("john")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.loadUserByUsername("john"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(USERNAME_HAS_BEEN_NOT_FOUND);
    }
}
