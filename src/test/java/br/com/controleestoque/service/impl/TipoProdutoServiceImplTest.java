package br.com.controleestoque.service.impl;

import br.com.controleestoque.exception.TipoProdutoException;
import br.com.controleestoque.model.dto.TipoProdutoDTO;
import br.com.controleestoque.model.entity.TipoProduto;
import br.com.controleestoque.repository.TipoProdutoRepository;
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

class TipoProdutoServiceImplTest {

    private static final UUID TIPO_PRODUTO_ID = UUID.randomUUID();
    private static final String NOME_PRODUTO = "Café";

    @Mock
    private TipoProdutoRepository tipoProdutoRepository;

    @InjectMocks
    private TipoProdutoServiceImpl tipoProdutoServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByIdSuccess() {
        TipoProduto tipoProdutoEntity = createTipoProdutoEntity(TIPO_PRODUTO_ID);

        when(tipoProdutoRepository.findById(TIPO_PRODUTO_ID)).thenReturn(Optional.of(tipoProdutoEntity));

        TipoProdutoDTO result = tipoProdutoServiceImpl.findById(TIPO_PRODUTO_ID);

        assertNotNull(result);
        assertEquals(TIPO_PRODUTO_ID, result.getUuid());
        assertEquals(NOME_PRODUTO, result.getNome());
    }

    @Test
    void testFindByIdNotFound() {
        when(tipoProdutoRepository.findById(TIPO_PRODUTO_ID)).thenReturn(Optional.empty());

        assertThrows(TipoProdutoException.class, () -> tipoProdutoServiceImpl.findById(TIPO_PRODUTO_ID));
    }

    @Test
    void testFindAllSuccess() {
        List<TipoProduto> tipoProdutoList = Collections.singletonList(createTipoProdutoEntity(TIPO_PRODUTO_ID));

        when(tipoProdutoRepository.findAll()).thenReturn(tipoProdutoList);

        List<TipoProdutoDTO> result = tipoProdutoServiceImpl.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TIPO_PRODUTO_ID, result.get(0).getUuid());
        assertEquals(NOME_PRODUTO, result.get(0).getNome());
    }

    @Test
    void testFindAllEmptyList() {
        when(tipoProdutoRepository.findAll()).thenReturn(Collections.emptyList());

        List<TipoProdutoDTO> result = tipoProdutoServiceImpl.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateSuccess() {
        TipoProdutoDTO tipoProdutoDTO = createTipoProdutoDTO(TIPO_PRODUTO_ID);
        TipoProduto tipoProdutoEntity = createTipoProdutoEntity(TIPO_PRODUTO_ID);

        when(tipoProdutoRepository.save(any(TipoProduto.class))).thenReturn(tipoProdutoEntity);

        TipoProdutoDTO result = tipoProdutoServiceImpl.create(tipoProdutoDTO);

        assertNotNull(result);
        assertEquals(TIPO_PRODUTO_ID, result.getUuid());
        assertEquals(NOME_PRODUTO, result.getNome());
    }

    @Test
    void testUpdateTipoProdutoSuccess() {
        TipoProdutoDTO tipoProdutoDTO = createTipoProdutoDTO(TIPO_PRODUTO_ID);
        TipoProduto tipoProdutoEntity = createTipoProdutoEntity(TIPO_PRODUTO_ID);

        when(tipoProdutoRepository.findById(TIPO_PRODUTO_ID)).thenReturn(Optional.of(tipoProdutoEntity));

        tipoProdutoServiceImpl.update(TIPO_PRODUTO_ID, tipoProdutoDTO);

        verify(tipoProdutoRepository).save(tipoProdutoEntity);
        assertEquals(NOME_PRODUTO, tipoProdutoEntity.getNome());
    }

    @Test
    void testUpdateNotFound() {
        TipoProdutoDTO tipoProdutoDTO = createTipoProdutoDTO(TIPO_PRODUTO_ID);

        when(tipoProdutoRepository.findById(TIPO_PRODUTO_ID)).thenReturn(Optional.empty());

        assertThrows(TipoProdutoException.class, () -> tipoProdutoServiceImpl.update(TIPO_PRODUTO_ID, tipoProdutoDTO));
    }

    @Test
    void testDeleteSuccess() {
        TipoProduto tipoProdutoEntity = createTipoProdutoEntity(TIPO_PRODUTO_ID);

        when(tipoProdutoRepository.findById(TIPO_PRODUTO_ID)).thenReturn(Optional.of(tipoProdutoEntity));

        tipoProdutoServiceImpl.delete(TIPO_PRODUTO_ID);

        verify(tipoProdutoRepository).delete(tipoProdutoEntity);
    }

    @Test
    void testDeleteNotFound() {
        when(tipoProdutoRepository.findById(TIPO_PRODUTO_ID)).thenReturn(Optional.empty());

        assertThrows(TipoProdutoException.class, () -> tipoProdutoServiceImpl.delete(TIPO_PRODUTO_ID));
    }

    private TipoProdutoDTO createTipoProdutoDTO(UUID uuid) {
        return TipoProdutoDTO.builder()
                .uuid(uuid)
                .nome(NOME_PRODUTO)
                .build();
    }

    private TipoProduto createTipoProdutoEntity(UUID uuid) {
        return TipoProduto.builder()
                .uuid(uuid)
                .nome(NOME_PRODUTO)
                .build();
    }
}