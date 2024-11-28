package br.com.controleestoque.controller;

import br.com.controleestoque.model.dto.PessoaDTO;
import br.com.controleestoque.service.PessoaService;
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

class PessoaControllerTest {

    private static final UUID PESSOA_ID = UUID.randomUUID();
    private static final PessoaDTO PESSOA_DTO = new PessoaDTO();

    @InjectMocks
    private PessoaController pessoaController;

    @Mock
    private PessoaService pessoaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByIdSuccess() {
        PESSOA_DTO.setUuid(PESSOA_ID);

        when(pessoaService.findById(PESSOA_ID)).thenReturn(PESSOA_DTO);

        ResponseEntity<PessoaDTO> response = pessoaController.findById(PESSOA_ID);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(PESSOA_ID, response.getBody().getUuid());
    }

    @Test
    void testFindAllSuccess() {
        when(pessoaService.findAll()).thenReturn(Collections.singletonList(PESSOA_DTO));

        ResponseEntity<List<PessoaDTO>> response = pessoaController.findAll();
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testCreatePessoaSuccess() {
        when(pessoaService.create(any(PessoaDTO.class))).thenReturn(PESSOA_DTO);

        ResponseEntity<PessoaDTO> response = pessoaController.create(PESSOA_DTO);
        assertEquals(201, response.getStatusCodeValue()); // Expecting 201 Created
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdatePessoaSuccess() {
        PESSOA_DTO.setUuid(PESSOA_ID);

        doNothing().when(pessoaService).update(eq(PESSOA_ID), any(PessoaDTO.class));

        ResponseEntity<Void> response = pessoaController.update(PESSOA_ID, PESSOA_DTO);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDeletePessoaSuccess() {
        doNothing().when(pessoaService).delete(PESSOA_ID);

        ResponseEntity<Void> response = pessoaController.delete(PESSOA_ID);
        assertEquals(204, response.getStatusCodeValue());
    }
}