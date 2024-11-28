package br.com.controleestoque.controller;

import br.com.controleestoque.model.dto.UserDTO;
import br.com.controleestoque.service.UserService;
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

class UserControllerTest {

    private static final UUID USER_ID = UUID.randomUUID();
    private static final UserDTO USER_DTO = new UserDTO();

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByIdSuccess() {
        USER_DTO.setUuid(USER_ID);

        when(userService.findById(USER_ID)).thenReturn(USER_DTO);

        ResponseEntity<UserDTO> response = userController.findById(USER_ID);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(USER_ID, response.getBody().getUuid());
    }

    @Test
    void testFindAllSuccess() {
        when(userService.findAll()).thenReturn(Collections.singletonList(USER_DTO));

        ResponseEntity<List<UserDTO>> response = userController.findAll();
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testCreateUserSuccess() {
        when(userService.create(any(UserDTO.class))).thenReturn(USER_DTO);

        ResponseEntity<UserDTO> response = userController.create(USER_DTO);
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdateUserSuccess() {
        USER_DTO.setUuid(USER_ID);

        doNothing().when(userService).update(eq(USER_ID), any(UserDTO.class));

        ResponseEntity<Void> response = userController.update(USER_ID, USER_DTO);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDeleteUserSuccess() {
        doNothing().when(userService).delete(USER_ID);

        ResponseEntity<Void> response = userController.delete(USER_ID);
        assertEquals(204, response.getStatusCodeValue());
    }
}