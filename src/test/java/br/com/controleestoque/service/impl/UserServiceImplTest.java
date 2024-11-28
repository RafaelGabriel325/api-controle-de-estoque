package br.com.controleestoque.service.impl;

import br.com.controleestoque.exception.UserException;
import br.com.controleestoque.model.dto.PermissionDTO;
import br.com.controleestoque.model.dto.UserDTO;
import br.com.controleestoque.model.entity.Permission;
import br.com.controleestoque.model.entity.User;
import br.com.controleestoque.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private static final UUID USER_ID = UUID.randomUUID();
    private static final String USERNAME = "rafal325";
    private static final String FULL_NAME = "Rafael Gabriel";
    private static final String PASSWORD = "rafael123";
    private static final String ENCODED_PASSWORD = "encodedPassword";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        User userEntity = createUserEntity(USER_ID);

        when(userRepository.findByUsername(USERNAME)).thenReturn(userEntity);

        UserDetails userDetails = userServiceImpl.loadUserByUsername(USERNAME);

        assertNotNull(userDetails);
        assertEquals(USERNAME, userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userServiceImpl.loadUserByUsername(USERNAME));
    }

    @Test
    void testFindByIdSuccess() {
        User userEntity = createUserEntity(USER_ID);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity));

        UserDTO result = userServiceImpl.findById(USER_ID);

        assertNotNull(result);
        assertEquals(USER_ID, result.getUuid());
        assertEquals(USERNAME, result.getUsername());
        assertEquals(FULL_NAME, result.getFullName());
    }

    @Test
    void testFindByIdNotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> userServiceImpl.findById(USER_ID));
    }

    @Test
    void testFindAllSuccess() {
        User userEntity = createUserEntity(USER_ID);
        List<User> userList = Collections.singletonList(userEntity);

        when(userRepository.findAll()).thenReturn(userList);

        List<UserDTO> result = userServiceImpl.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(USER_ID, result.get(0).getUuid());
        assertEquals(USERNAME, result.get(0).getUsername());
        assertEquals(FULL_NAME, result.get(0).getFullName());
    }

    @Test
    void testFindAllEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserDTO> result = userServiceImpl.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateSuccess() {
        UserDTO userDTO = createUserDTO(USER_ID);
        User userEntity = createUserEntity(USER_ID);

        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(userEntity);

        UserDTO result = userServiceImpl.create(userDTO);

        assertNotNull(result);
        assertEquals(USER_ID, result.getUuid());
        assertEquals(USERNAME, result.getUsername());
    }

    @Test
    void testUpdateSuccess() {
        UserDTO userDTO = createUserDTO(USER_ID);
        User userEntity = createUserEntity(USER_ID);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);

        userServiceImpl.update(USER_ID, userDTO);

        verify(userRepository).save(userEntity);
        assertEquals(USERNAME, userEntity.getUsername());
        assertEquals(FULL_NAME, userEntity.getFullName());
    }

    @Test
    void testUpdateNotFound() {
        UserDTO userDTO = createUserDTO(USER_ID);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> userServiceImpl.update(USER_ID, userDTO));
    }

    @Test
    void testDeleteSuccess() {
        User userEntity = createUserEntity(USER_ID);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity));

        userServiceImpl.delete(USER_ID);

        verify(userRepository).delete(userEntity);
    }

    @Test
    void testDeleteNotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> userServiceImpl.delete(USER_ID));
    }

    private UserDTO createUserDTO(UUID uuid) {
        List<PermissionDTO> permissionDTOList = createPermissionDTOList();
        return UserDTO.builder()
                .uuid(uuid)
                .username(USERNAME)
                .fullName(FULL_NAME)
                .password(PASSWORD)
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
                .username(USERNAME)
                .fullName(FULL_NAME)
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

    private List<PermissionDTO> createPermissionDTOList() {
        List<PermissionDTO> permissions = new ArrayList<>();
        permissions.add(PermissionDTO.builder().uuid(UUID.randomUUID()).description("ADM").build());
        permissions.add(PermissionDTO.builder().uuid(UUID.randomUUID()).description("TESTER").build());
        return permissions;
    }
}