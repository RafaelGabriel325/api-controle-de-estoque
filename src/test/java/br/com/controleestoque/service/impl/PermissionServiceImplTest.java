package br.com.controleestoque.service.impl;

import br.com.controleestoque.exception.PermissionException;
import br.com.controleestoque.model.dto.PermissionDTO;
import br.com.controleestoque.model.entity.Permission;
import br.com.controleestoque.repository.PermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PermissionServiceImplTest {

    private static final UUID PERMISSION_ID = UUID.randomUUID();
    private static final String DESCRIPTION = "Admin";

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionServiceImpl permissionServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByIdSuccess() {
        Permission permissionEntity = createPermissionEntity(PERMISSION_ID);

        when(permissionRepository.findById(PERMISSION_ID)).thenReturn(Optional.of(permissionEntity));

        PermissionDTO result = permissionServiceImpl.findById(PERMISSION_ID);

        assertNotNull(result);
        assertEquals(PERMISSION_ID, result.getUuid());
        assertEquals(DESCRIPTION, result.getDescription());
    }

    @Test
    void testFindByIdNotFound() {
        when(permissionRepository.findById(PERMISSION_ID)).thenReturn(Optional.empty());

        assertThrows(PermissionException.class, () -> permissionServiceImpl.findById(PERMISSION_ID));
    }

    @Test
    void testFindAllSuccess() {
        List<Permission> permissionList = Collections.singletonList(createPermissionEntity(PERMISSION_ID));

        when(permissionRepository.findAll()).thenReturn(permissionList);

        List<PermissionDTO> result = permissionServiceImpl.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(PERMISSION_ID, result.get(0).getUuid());
        assertEquals(DESCRIPTION, result.get(0).getDescription());
    }

    @Test
    void testFindAllEmptyList() {
        when(permissionRepository.findAll()).thenReturn(Collections.emptyList());

        List<PermissionDTO> result = permissionServiceImpl.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateSuccess() {
        PermissionDTO permissionDTO = createPermissionDTO(PERMISSION_ID);
        Permission permissionEntity = createPermissionEntity(PERMISSION_ID);

        when(permissionRepository.save(any(Permission.class))).thenReturn(permissionEntity);

        PermissionDTO result = permissionServiceImpl.create(permissionDTO);

        assertNotNull(result);
        assertNotNull(result.getUuid());
        assertEquals(DESCRIPTION, result.getDescription());
    }

    @Test
    void testUpdateSuccess() {
        PermissionDTO permissionDTO = createPermissionDTO(PERMISSION_ID);
        Permission permissionEntity = createPermissionEntity(PERMISSION_ID);

        when(permissionRepository.findById(PERMISSION_ID)).thenReturn(Optional.of(permissionEntity));
        when(permissionRepository.save(any(Permission.class))).thenReturn(permissionEntity);

        permissionServiceImpl.update(PERMISSION_ID, permissionDTO);

        assertEquals(DESCRIPTION, permissionEntity.getDescription());
    }

    @Test
    void testUpdateNotFound() {
        PermissionDTO permissionDTO = createPermissionDTO(PERMISSION_ID);

        when(permissionRepository.findById(PERMISSION_ID)).thenReturn(Optional.empty());

        assertThrows(PermissionException.class, () -> permissionServiceImpl.update(PERMISSION_ID, permissionDTO));
    }

    @Test
    void testDeleteSuccess() {
        Permission permissionEntity = createPermissionEntity(PERMISSION_ID);

        when(permissionRepository.findById(PERMISSION_ID)).thenReturn(Optional.of(permissionEntity));

        permissionServiceImpl.delete(PERMISSION_ID);

        verify(permissionRepository).delete(permissionEntity);
    }

    @Test
    void testDeleteNotFound() {
        when(permissionRepository.findById(PERMISSION_ID)).thenReturn(Optional.empty());

        assertThrows(PermissionException.class, () -> permissionServiceImpl.delete(PERMISSION_ID));
    }

    private PermissionDTO createPermissionDTO(UUID uuid) {
        return PermissionDTO.builder()
                .uuid(uuid)
                .description(DESCRIPTION)
                .build();
    }

    private Permission createPermissionEntity(UUID uuid) {
        return Permission.builder()
                .uuid(uuid)
                .description(DESCRIPTION)
                .build();
    }
}