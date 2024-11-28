package br.com.controleestoque.service.impl;

import br.com.controleestoque.model.entity.Permission;
import br.com.controleestoque.model.entity.User;
import br.com.controleestoque.repository.UserRepository;
import br.com.controleestoque.security.AccountCredentialsDTO;
import br.com.controleestoque.security.TokenDTO;
import br.com.controleestoque.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AuthServiceImplTest {

    private static final UUID USER_UUID = UUID.randomUUID();
    private static final String USERNAME = "rafal325";
    private static final String PASSWORD = "rafael123";
    private static final String ACCESS_TOKEN = "testAccessToken";
    private static final String REFRESH_TOKEN = "testRefreshToken";
    private static final String VALID_REFRESH_TOKEN = "validRefreshToken";
    private static final String NON_EXISTENT_USERNAME = "nonexistentUser";

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignInValidCredentials() {
        User userEntity = createUserEntity();
        AccountCredentialsDTO accountCredentialsDTO = new AccountCredentialsDTO(USERNAME, PASSWORD);

        when(userRepository.findByUsername(USERNAME)).thenReturn(userEntity);
        when(passwordEncoder.matches(PASSWORD, userEntity.getPassword())).thenReturn(true);
        when(jwtTokenProvider.createAccessToken(USERNAME, userEntity.getPermissions())).thenReturn(createTokenDTO());

        TokenDTO result = authService.signIn(accountCredentialsDTO);

        assertNotNull(result);
        assertEquals(ACCESS_TOKEN, result.getAccessToken());
        assertEquals(REFRESH_TOKEN, result.getRefreshToken());
    }

    @Test
    void testSignInInvalidCredentials() {
        User userEntity = createUserEntity();
        AccountCredentialsDTO accountCredentialsDTO = new AccountCredentialsDTO(USERNAME, PASSWORD);

        when(userRepository.findByUsername(USERNAME)).thenReturn(userEntity);
        when(passwordEncoder.matches(PASSWORD, userEntity.getPassword())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authService.signIn(accountCredentialsDTO));
    }

    @Test
    void testRefreshTokenValidUser() {
        User userEntity = createUserEntity();

        when(userRepository.findByUsername(USERNAME)).thenReturn(userEntity);
        when(jwtTokenProvider.createRefreshToken(VALID_REFRESH_TOKEN)).thenReturn(createTokenDTO());

        TokenDTO result = authService.refreshToken(USERNAME, VALID_REFRESH_TOKEN);

        assertNotNull(result);
        assertEquals(ACCESS_TOKEN, result.getAccessToken());
        assertEquals(REFRESH_TOKEN, result.getRefreshToken());
    }

    @Test
    void testRefreshTokenInvalidUser() {
        when(userRepository.findByUsername(NON_EXISTENT_USERNAME)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> authService.refreshToken(NON_EXISTENT_USERNAME, VALID_REFRESH_TOKEN));
    }

    private User createUserEntity() {
        List<Permission> permissionList = createPermissionList();
        return User.builder()
                .uuid(USER_UUID)
                .username(USERNAME)
                .fullName("Rafael Gabriel")
                .password(PASSWORD)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enable(true)
                .permissions(permissionList)
                .build();
    }

    private List<Permission> createPermissionList() {
        List<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.builder().uuid(UUID.randomUUID()).description("ADM").build());
        permissions.add(Permission.builder().uuid(UUID.randomUUID()).description("TESTER").build());
        return permissions;
    }

    private TokenDTO createTokenDTO() {
        return TokenDTO.builder()
                .accessToken(ACCESS_TOKEN)
                .refreshToken(REFRESH_TOKEN)
                .build();
    }
}