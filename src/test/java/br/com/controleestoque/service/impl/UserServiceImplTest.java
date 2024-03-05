package br.com.controleestoque.service.impl;

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
        User userEntity = createUserEntity(UUID.randomUUID());

        when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(userEntity);

        UserDetails userDetails = userServiceImpl.loadUserByUsername(userEntity.getUsername());

        assertNotNull(userDetails);
        assertEquals(userEntity.getUsername(), userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        String username = "nonExistingUser";

        when(userRepository.findByUsername(username)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userServiceImpl.loadUserByUsername(username));
    }

    @Test
    void testFindByIdSuccess() {
        UUID userId = UUID.randomUUID();
        User userEntity = createUserEntity(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        UserDTO result = userServiceImpl.findById(userId);

        assertNotNull(result);
        assertEquals(userEntity.getUuid(), result.getUuid());
        assertEquals(userEntity.getUsername(), result.getUsername());
        assertEquals(userEntity.getFullName(), result.getFullName());
    }

    @Test
    void testFindByIdNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userServiceImpl.findById(userId));
    }

    @Test
    void testFindAllSuccess() {
        List<User> userList = new ArrayList<>();

        UUID userId = UUID.randomUUID();
        User userEntity = createUserEntity(userId);

        userList.add(userEntity);

        when(userRepository.findAll()).thenReturn(userList);

        List<UserDTO> result = userServiceImpl.findAll();

        assertNotNull(result);
        assertEquals(userList.get(0).getUuid(), result.get(0).getUuid());
        assertEquals(userList.get(0).getUsername(), result.get(0).getUsername());
        assertEquals(userList.get(0).getFullName(), result.get(0).getFullName());
        assertEquals(userList.size(), result.size());
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
        UserDTO userDTO = createUserDTO(UUID.randomUUID());
        User userEntity = createUserEntity(userDTO.getUuid());

        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(userEntity);

        UserDTO result = userServiceImpl.create(userDTO);

        assertNotNull(result);
        assertNotNull(result.getUuid());
        assertEquals(userDTO.getUsername(), result.getUsername());
    }

    @Test
    void testUpdateSuccess() {
        UUID userId = UUID.randomUUID();
        UserDTO userDTO = createUserDTO(userId);
        User userEntity = createUserEntity(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(userEntity);

        userServiceImpl.update(userId, userDTO);

        assertEquals(userDTO.getUsername(), userEntity.getUsername());
        assertEquals(userDTO.getFullName(), userEntity.getFullName());
    }

    @Test
    void testUpdateNotFound() {
        UUID userId = UUID.randomUUID();
        UserDTO userDTO = createUserDTO(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userServiceImpl.update(userId, userDTO));
    }

    @Test
    void testDeleteSuccess() {
        UUID userId = UUID.randomUUID();
        User userEntity = createUserEntity(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        userServiceImpl.delete(userId);

        verify(userRepository).delete(userEntity);
    }

    @Test
    void testDeleteNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userServiceImpl.delete(userId));
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
}
