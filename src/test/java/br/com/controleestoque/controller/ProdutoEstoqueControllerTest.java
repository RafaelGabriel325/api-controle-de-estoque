package br.com.controleestoque.controller;

import br.com.controleestoque.model.dto.ProdutoEstoqueDTO;
import br.com.controleestoque.service.ProdutoEstoqueService;
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

class ProdutoEstoqueControllerTest {

    private static final UUID PRODUTO_ID = UUID.randomUUID();
    private static final ProdutoEstoqueDTO PRODUTO_DTO = new ProdutoEstoqueDTO();

    @InjectMocks
    private ProdutoEstoqueController produtoEstoqueController;

    @Mock
    private ProdutoEstoqueService produtoEstoqueService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByIdSuccess() {
        PRODUTO_DTO.setUuid(PRODUTO_ID);

        when(produtoEstoqueService.findById(PRODUTO_ID)).thenReturn(PRODUTO_DTO);

        ResponseEntity<ProdutoEstoqueDTO> response = produtoEstoqueController.findById(PRODUTO_ID);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(PRODUTO_ID, response.getBody().getUuid());
    }

    @Test
    void testFindAllSuccess() {
        when(produtoEstoqueService.findAll()).thenReturn(Collections.singletonList(PRODUTO_DTO));

        ResponseEntity<List<ProdutoEstoqueDTO>> response = produtoEstoqueController.findAll();
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testCreateProdutoSuccess() {
        when(produtoEstoqueService.create(any(ProdutoEstoqueDTO.class))).thenReturn(PRODUTO_DTO);

        ResponseEntity<ProdutoEstoqueDTO> response = produtoEstoqueController.create(PRODUTO_DTO);
        assertEquals(201, response.getStatusCodeValue()); // Expecting 201 Created
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdateProdutoSuccess() {
        PRODUTO_DTO.setUuid(PRODUTO_ID);

        doNothing().when(produtoEstoqueService).update(eq(PRODUTO_ID), any(ProdutoEstoqueDTO.class));

        ResponseEntity<Void> response = produtoEstoqueController.update(PRODUTO_ID, PRODUTO_DTO);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDeleteProdutoSuccess() {
        doNothing().when(produtoEstoqueService).delete(PRODUTO_ID);

        ResponseEntity<Void> response = produtoEstoqueController.delete(PRODUTO_ID);
        assertEquals(204, response.getStatusCodeValue());
    }
}