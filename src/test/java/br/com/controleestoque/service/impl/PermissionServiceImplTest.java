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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PermissionServiceImplTest {

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
        UUID permissionId = UUID.randomUUID();
        Permission permissionEntity = createPermissionEntity(permissionId);

        when(permissionRepository.findById(permissionEntity.getUuid())).thenReturn(Optional.of(permissionEntity));

        PermissionDTO result = permissionServiceImpl.findById(permissionId);

        assertNotNull(result);
        assertEquals(permissionEntity.getUuid(), result.getUuid());
        assertEquals(permissionEntity.getDescription(), result.getDescription());
    }

    @Test
    void testFindByIdNotFound() {
        UUID permissionId = UUID.randomUUID();
        when(permissionRepository.findById(permissionId)).thenReturn(Optional.empty());

        assertThrows(PermissionException.class, () -> permissionServiceImpl.findById(permissionId));
    }

    @Test
    void testFindAllSuccess() {
        List<Permission> permissionList = new ArrayList<>();

        UUID permissionId = UUID.randomUUID();
        Permission permissionEntity = createPermissionEntity(permissionId);

        permissionList.add(permissionEntity);

        when(permissionRepository.findAll()).thenReturn(permissionList);

        List<PermissionDTO> result = permissionServiceImpl.findAll();

        assertNotNull(result);
        assertEquals(permissionList.get(0).getUuid(), result.get(0).getUuid());
        assertEquals(permissionList.get(0).getDescription(), result.get(0).getDescription());
        assertEquals(permissionList.size(), result.size());
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
        PermissionDTO permissionDTO = createPermissionDTO(UUID.randomUUID());
        Permission permissionEntity = createPermissionEntity(permissionDTO.getUuid());

        when(permissionRepository.save(any(Permission.class))).thenReturn(permissionEntity);

        PermissionDTO result = permissionServiceImpl.create(permissionDTO);

        assertNotNull(result);
        assertNotNull(result.getUuid());
        assertEquals(permissionDTO.getDescription(), result.getDescription());
    }

    @Test
    public void testUpdatePessoaSucces() {
        UUID id = UUID.randomUUID();

        PermissionDTO permissionDTO = createPermissionDTO(id);
        Permission permissionEntity = createPermissionEntity(id);

        when(permissionRepository.findById(permissionEntity.getUuid())).thenReturn(Optional.of(permissionEntity));
        when(permissionRepository.save(any(Permission.class))).thenReturn(permissionEntity);

        permissionServiceImpl.update(id, permissionDTO);

        assertEquals(permissionDTO.getDescription(), permissionEntity.getDescription());
    }

    @Test
    void testUpdateNotFound() {
        UUID permissionId = UUID.randomUUID();

        PermissionDTO permissionDTO = createPermissionDTO(permissionId);

        when(permissionRepository.findById(permissionId)).thenReturn(Optional.empty());

        assertThrows(PermissionException.class, () -> permissionServiceImpl.update(permissionId, permissionDTO));
    }

    @Test
    void testDeleteSuccess() {
        UUID permissionId = UUID.randomUUID();
        PermissionDTO permissionDTO = createPermissionDTO(permissionId);
        Permission permissionEntity = createPermissionEntity(permissionId);

        when(permissionRepository.findById(permissionEntity.getUuid())).thenReturn(Optional.of(permissionEntity));

        permissionServiceImpl.delete(permissionDTO.getUuid());

        verify(permissionRepository).delete(permissionEntity);
    }

    @Test
    void testDeleteNotFound() {
        UUID permissionId = UUID.randomUUID();
        when(permissionRepository.findById(permissionId)).thenReturn(Optional.empty());

        assertThrows(PermissionException.class, () -> permissionServiceImpl.delete(permissionId));
    }

    private PermissionDTO createPermissionDTO(UUID uuid) {
        return PermissionDTO.builder()
                .uuid(uuid)
                .description("Admin")
                .build();
    }

    private Permission createPermissionEntity(UUID uuid) {
        return Permission.builder()
                .uuid(uuid)
                .description("Admin")
                .build();
    }
}