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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TipoProdutoServiceImplTest {

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
        UUID tipoProdutoId = UUID.randomUUID();
        TipoProduto tipoProdutoEntity = createTipoProdutoEntity(tipoProdutoId);

        when(tipoProdutoRepository.findById(tipoProdutoId)).thenReturn(Optional.of(tipoProdutoEntity));

        TipoProdutoDTO result = tipoProdutoServiceImpl.findById(tipoProdutoId);

        assertNotNull(result);
        assertEquals(tipoProdutoEntity.getUuid(), result.getUuid());
        assertEquals(tipoProdutoEntity.getNome(), result.getNome());
    }

    @Test
    void testFindByIdNotFound() {
        UUID tipoProdutoId = UUID.randomUUID();
        when(tipoProdutoRepository.findById(tipoProdutoId)).thenReturn(Optional.empty());

        assertThrows(TipoProdutoException.class, () -> tipoProdutoServiceImpl.findById(tipoProdutoId));
    }

    @Test
    void testFindAllSuccess() {
        List<TipoProduto> tipoProdutoList = new ArrayList<>();

        UUID tipoProdutoId = UUID.randomUUID();
        TipoProduto tipoProdutoEntity = createTipoProdutoEntity(tipoProdutoId);

        tipoProdutoList.add(tipoProdutoEntity);

        when(tipoProdutoRepository.findAll()).thenReturn(tipoProdutoList);

        List<TipoProdutoDTO> result = tipoProdutoServiceImpl.findAll();

        assertNotNull(result);
        assertEquals(tipoProdutoList.get(0).getUuid(), result.get(0).getUuid());
        assertEquals(tipoProdutoList.get(0).getNome(), result.get(0).getNome());
        assertEquals(tipoProdutoList.size(), result.size());
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
        TipoProdutoDTO tipoProdutoDTO = createTipoProdutoDTO(UUID.randomUUID());
        TipoProduto tipoProdutoEntity = createTipoProdutoEntity(tipoProdutoDTO.getUuid());

        when(tipoProdutoRepository.save(any(TipoProduto.class))).thenReturn(tipoProdutoEntity);

        TipoProdutoDTO result = tipoProdutoServiceImpl.create(tipoProdutoDTO);

        assertNotNull(result);
        assertNotNull(result.getUuid());
        assertEquals(tipoProdutoDTO.getNome(), result.getNome());
    }

    @Test
    public void testUpdateTipoProdutoSucces() {
        UUID id = UUID.randomUUID();

        TipoProdutoDTO tipoProdutoDTO = createTipoProdutoDTO(id);
        TipoProduto tipoProdutoEntity = createTipoProdutoEntity(id);

        when(tipoProdutoRepository.findById(tipoProdutoEntity.getUuid())).thenReturn(Optional.of(tipoProdutoEntity));
        when(tipoProdutoRepository.save(any(TipoProduto.class))).thenReturn(tipoProdutoEntity);

        tipoProdutoServiceImpl.update(id, tipoProdutoDTO);

        assertEquals(tipoProdutoDTO.getNome(), tipoProdutoEntity.getNome());
    }

    @Test
    void testUpdateNotFound() {
        UUID tipoProdutoId = UUID.randomUUID();

        TipoProdutoDTO tipoProdutoDTO = createTipoProdutoDTO(tipoProdutoId);

        when(tipoProdutoRepository.findById(tipoProdutoId)).thenReturn(Optional.empty());

        assertThrows(TipoProdutoException.class, () -> tipoProdutoServiceImpl.update(tipoProdutoId, tipoProdutoDTO));
    }

    @Test
    void testDeleteSuccess() {
        UUID tipoProdutoId = UUID.randomUUID();
        TipoProdutoDTO tipoProdutoDTO = createTipoProdutoDTO(tipoProdutoId);
        TipoProduto tipoProdutoEntity = createTipoProdutoEntity(tipoProdutoId);

        when(tipoProdutoRepository.findById(tipoProdutoEntity.getUuid())).thenReturn(Optional.of(tipoProdutoEntity));

        tipoProdutoServiceImpl.delete(tipoProdutoDTO.getUuid());

        verify(tipoProdutoRepository).delete(tipoProdutoEntity);
    }

    @Test
    void testDeleteNotFound() {
        UUID tipoProdutoId = UUID.randomUUID();
        when(tipoProdutoRepository.findById(tipoProdutoId)).thenReturn(Optional.empty());

        assertThrows(TipoProdutoException.class, () -> tipoProdutoServiceImpl.delete(tipoProdutoId));
    }

    private TipoProdutoDTO createTipoProdutoDTO(UUID uuid) {
        return TipoProdutoDTO.builder()
                .uuid(uuid)
                .nome("Café")
                .build();
    }

    private TipoProduto createTipoProdutoEntity(UUID uuid) {
        return TipoProduto.builder()
                .uuid(uuid)
                .nome("Café")
                .build();
    }
}
