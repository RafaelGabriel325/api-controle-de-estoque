package br.com.controleestoque.service.impl;

import br.com.controleestoque.model.dto.PermissionDTO;
import br.com.controleestoque.model.dto.UserDTO;
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
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSignInValidCredentials() {
        UUID userUuid = UUID.randomUUID();
        UserDTO userDTO = createUserDTO(userUuid);
        User userEntity = createUserEntity(userUuid);
        AccountCredentialsDTO accountCredentialsDTO = new AccountCredentialsDTO(userDTO.getUsername(), userDTO.getPassword());

        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(userEntity);
        when(passwordEncoder.matches(accountCredentialsDTO.getPassword(), userEntity.getPassword())).thenReturn(true);
        when(jwtTokenProvider.createAccessToken(userDTO.getUsername(), userEntity.getPermissions())).thenReturn(createTokenDTO());

        TokenDTO result = authService.signIn(accountCredentialsDTO);

        assertNotNull(result);
        assertEquals("testAccessToken", result.getAccessToken());
        assertEquals("testRefreshToken", result.getRefreshToken());
    }

    @Test
    void testSignInInvalidCredentials() {
        UUID userUuid = UUID.randomUUID();
        UserDTO userDTO = createUserDTO(userUuid);
        User userEntity = createUserEntity(userUuid);

        AccountCredentialsDTO accountCredentialsDTO = new AccountCredentialsDTO(userDTO.getUsername(), userDTO.getPassword());

        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(userEntity);
        when(passwordEncoder.matches(accountCredentialsDTO.getPassword(), userEntity.getPassword())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authService.signIn(accountCredentialsDTO));
    }


    @Test
    void testRefreshTokenValidUser() {
        UUID userUuid = UUID.randomUUID();
        User userEntity = createUserEntity(userUuid);
        String refreshToken = "validRefreshToken";

        when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(userEntity);
        when(jwtTokenProvider.createRefreshToken(refreshToken)).thenReturn(createTokenDTO());

        TokenDTO result = authService.refreshToken(userEntity.getUsername(), refreshToken);

        assertNotNull(result);
        assertEquals("testAccessToken", result.getAccessToken());
        assertEquals("testRefreshToken", result.getRefreshToken());
    }

    @Test
    void testRefreshTokenInvalidUser() {
        String nonExistentUsername = "nonexistentUser";
        String refreshToken = "validRefreshToken";

        when(userRepository.findByUsername(nonExistentUsername)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> authService.refreshToken(nonExistentUsername, refreshToken));
    }

    private UserDTO createUserDTO(UUID uuid) {
        List<PermissionDTO> permissionDTOList = createPermissionDTOList();
        return UserDTO.builder()
                .uuid(uuid)
                .username("rafal325")
                .fullName("Rafael Gabriel")
                .password("rafael123")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enable(true)
                .permissions(permissionDTOList)
                .build();
    }

    private User createUserEntity(UUID uuid) {
        List<Permission> permissionList = createPermissionList();
        return User.builder()
                .uuid(uuid)
                .username("rafal325")
                .fullName("Rafael Gabriel")
                .password("rafael123")
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

    private List<PermissionDTO> createPermissionDTOList() {
        List<PermissionDTO> permissions = new ArrayList<>();
        permissions.add(PermissionDTO.builder().uuid(UUID.randomUUID()).description("ADM").build());
        permissions.add(PermissionDTO.builder().uuid(UUID.randomUUID()).description("TESTER").build());
        return permissions;
    }

    private TokenDTO createTokenDTO(){
        return TokenDTO.builder()
                .accessToken("testAccessToken")
                .refreshToken("testRefreshToken")
                .build();
    }
}
