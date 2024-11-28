package br.com.controleestoque.controller;

import br.com.controleestoque.model.dto.TipoProdutoDTO;
import br.com.controleestoque.service.TipoProdutoService;
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

class TipoProdutoControllerTest {

    private static final UUID TIPO_PRODUTO_ID = UUID.randomUUID();
    private static final TipoProdutoDTO TIPO_PRODUTO_DTO = new TipoProdutoDTO();

    @InjectMocks
    private TipoProdutoController tipoProdutoController;

    @Mock
    private TipoProdutoService tipoProdutoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByIdSuccess() {
        TIPO_PRODUTO_DTO.setUuid(TIPO_PRODUTO_ID);

        when(tipoProdutoService.findById(TIPO_PRODUTO_ID)).thenReturn(TIPO_PRODUTO_DTO);

        ResponseEntity<TipoProdutoDTO> response = tipoProdutoController.findById(TIPO_PRODUTO_ID);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(TIPO_PRODUTO_ID, response.getBody().getUuid());
    }

    @Test
    void testFindAllSuccess() {
        when(tipoProdutoService.findAll()).thenReturn(Collections.singletonList(TIPO_PRODUTO_DTO));

        ResponseEntity<List<TipoProdutoDTO>> response = tipoProdutoController.findAll();
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testCreateTipoProdutoSuccess() {
        when(tipoProdutoService.create(any(TipoProdutoDTO.class))).thenReturn(TIPO_PRODUTO_DTO);

        ResponseEntity<TipoProdutoDTO> response = tipoProdutoController.create(TIPO_PRODUTO_DTO);
        assertEquals(201, response.getStatusCodeValue()); // Expecting 201 Created
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdateTipoProdutoSuccess() {
        TIPO_PRODUTO_DTO.setUuid(TIPO_PRODUTO_ID);

        doNothing().when(tipoProdutoService).update(eq(TIPO_PRODUTO_ID), any(TipoProdutoDTO.class));

        ResponseEntity<Void> response = tipoProdutoController.update(TIPO_PRODUTO_ID, TIPO_PRODUTO_DTO);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDeleteTipoProdutoSuccess() {
        doNothing().when(tipoProdutoService).delete(TIPO_PRODUTO_ID);

        ResponseEntity<Void> response = tipoProdutoController.delete(TIPO_PRODUTO_ID);
        assertEquals(204, response.getStatusCodeValue());
    }
}