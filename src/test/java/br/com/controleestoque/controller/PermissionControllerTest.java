package br.com.controleestoque.controller;

import br.com.controleestoque.model.dto.PermissionDTO;
import br.com.controleestoque.service.PermissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class PermissionControllerTest {

    private static final UUID PERMISSION_ID = UUID.randomUUID();
    private static final PermissionDTO PERMISSION_DTO = new PermissionDTO();

    @InjectMocks
    private PermissionController permissionController;

    @Mock
    private PermissionService permissionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByIdSuccess() {
        PERMISSION_DTO.setUuid(PERMISSION_ID);

        when(permissionService.findById(PERMISSION_ID)).thenReturn(PERMISSION_DTO);

        ResponseEntity<PermissionDTO> response = permissionController.findById(PERMISSION_ID);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(PERMISSION_ID, response.getBody().getUuid());
    }

    @Test
    void testFindAllSuccess() {
        when(permissionService.findAll()).thenReturn(Collections.singletonList(PERMISSION_DTO));

        ResponseEntity<List<PermissionDTO>> response = permissionController.findAll();
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testCreatePermissionSuccess() {
        when(permissionService.create(any(PermissionDTO.class))).thenReturn(PERMISSION_DTO);

        ResponseEntity<PermissionDTO> response = permissionController.create(PERMISSION_DTO);
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdatePermissionSuccess() {
        PERMISSION_DTO.setUuid(PERMISSION_ID);

        doNothing().when(permissionService).update(eq(PERMISSION_ID), any(PermissionDTO.class));

        ResponseEntity<Void> response = permissionController.update(PERMISSION_ID, PERMISSION_DTO);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDeletePermissionSuccess() {
        doNothing().when(permissionService).delete(PERMISSION_ID);

        ResponseEntity<Void> response = permissionController.delete(PERMISSION_ID);
        assertEquals(204, response.getStatusCodeValue());
    }
}