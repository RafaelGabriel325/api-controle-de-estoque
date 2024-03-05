package br.com.controleestoque.service.impl;

import br.com.controleestoque.exception.ProdutoEstoqueException;
import br.com.controleestoque.model.dto.ProdutoEstoqueDTO;
import br.com.controleestoque.model.entity.Pessoa;
import br.com.controleestoque.model.entity.ProdutoEstoque;
import br.com.controleestoque.model.entity.TipoProduto;
import br.com.controleestoque.model.mapper.ProdutoEstoqueMapper;
import br.com.controleestoque.repository.PessoaRepository;
import br.com.controleestoque.repository.ProdutoEstoqueRepository;
import br.com.controleestoque.repository.TipoProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoEstoqueServiceImplTest {

    @Mock
    private ProdutoEstoqueRepository produtoEstoqueRepository;

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private TipoProdutoRepository tipoProdutoRepository;

    @InjectMocks
    private ProdutoEstoqueServiceImpl produtoEstoqueServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByIdSuccess() {
        UUID produtoEstoqueId = UUID.randomUUID();
        ProdutoEstoqueDTO produtoEstoqueDTO = createProdutoEstoqueDTO(produtoEstoqueId);
        ProdutoEstoque produtoEstoqueEntity = ProdutoEstoqueMapper.INSTANCE.dtoToEntity(produtoEstoqueDTO);

        when(produtoEstoqueRepository.findById(produtoEstoqueId)).thenReturn(Optional.of(produtoEstoqueEntity));

        ProdutoEstoqueDTO result = produtoEstoqueServiceImpl.findById(produtoEstoqueId);

        assertNotNull(result);
        assertEquals(produtoEstoqueEntity.getUuid(), result.getUuid());
        assertEquals(produtoEstoqueEntity.getMarca(), result.getMarca());
        assertEquals(produtoEstoqueEntity.getQuantidadePacote(), result.getQuantidadePacote());
    }

    @Test
    void testFindByIdNotFound() {
        UUID produtoEstoqueId = UUID.randomUUID();
        when(produtoEstoqueRepository.findById(produtoEstoqueId)).thenReturn(Optional.empty());

        assertThrows(ProdutoEstoqueException.class, () -> produtoEstoqueServiceImpl.findById(produtoEstoqueId));
    }

    @Test
    void testFindAllSuccess() {
        List<ProdutoEstoque> produtoEstoqueList = Collections.singletonList(createProdutoEstoqueEntity(UUID.randomUUID()));

        when(produtoEstoqueRepository.findAll()).thenReturn(produtoEstoqueList);

        List<ProdutoEstoqueDTO> result = produtoEstoqueServiceImpl.findAll();

        assertNotNull(result);
        assertEquals(produtoEstoqueList.size(), result.size());
        assertEquals(produtoEstoqueList.get(0).getUuid(), result.get(0).getUuid());
        assertEquals(produtoEstoqueList.get(0).getMarca(), result.get(0).getMarca());
        assertEquals(produtoEstoqueList.get(0).getQuantidadePacote(), result.get(0).getQuantidadePacote());
    }

    @Test
    void testFindAllEmptyList() {
        when(produtoEstoqueRepository.findAll()).thenReturn(Collections.emptyList());

        List<ProdutoEstoqueDTO> result = produtoEstoqueServiceImpl.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateSuccess() {
        ProdutoEstoqueDTO produtoEstoqueDTO = createProdutoEstoqueDTO(UUID.randomUUID());
        ProdutoEstoque produtoEstoqueEntity = ProdutoEstoqueMapper.INSTANCE.dtoToEntity(produtoEstoqueDTO);

        Pessoa pessoaEntity = createPessoaEntity(UUID.randomUUID());
        when(pessoaRepository.findById(produtoEstoqueDTO.getPessoa().getUuid())).thenReturn(Optional.of(pessoaEntity));

        TipoProduto tipoProdutoEntity = createTipoProdutoEntity(UUID.randomUUID());
        when(tipoProdutoRepository.findById(produtoEstoqueDTO.getTipoProduto().getUuid())).thenReturn(Optional.of(tipoProdutoEntity));

        when(produtoEstoqueRepository.save(any(ProdutoEstoque.class))).thenReturn(produtoEstoqueEntity);

        ProdutoEstoqueDTO result = produtoEstoqueServiceImpl.create(produtoEstoqueDTO);

        assertNotNull(result);
        assertNotNull(result.getUuid());
        assertEquals(produtoEstoqueDTO.getMarca(), result.getMarca());
        assertEquals(produtoEstoqueDTO.getQuantidadePacote(), result.getQuantidadePacote());
    }

    @Test
    void testUpdateProdutoEstoqueSuccess() {
        UUID produtoEstoqueId = UUID.randomUUID();
        ProdutoEstoqueDTO produtoEstoqueDTO = createProdutoEstoqueDTO(produtoEstoqueId);
        ProdutoEstoque produtoEstoqueEntity = ProdutoEstoqueMapper.INSTANCE.dtoToEntity(produtoEstoqueDTO);

        when(produtoEstoqueRepository.findById(produtoEstoqueEntity.getUuid())).thenReturn(Optional.of(produtoEstoqueEntity));
        when(produtoEstoqueRepository.save(any(ProdutoEstoque.class))).thenReturn(produtoEstoqueEntity);

        produtoEstoqueServiceImpl.update(produtoEstoqueId, produtoEstoqueDTO);

        assertEquals(produtoEstoqueDTO.getMarca(), produtoEstoqueEntity.getMarca());
        assertEquals(produtoEstoqueDTO.getQuantidadePacote(), produtoEstoqueEntity.getQuantidadePacote());
    }

    @Test
    void testUpdateNotFound() {
        UUID produtoEstoqueId = UUID.randomUUID();
        ProdutoEstoqueDTO produtoEstoqueDTO = createProdutoEstoqueDTO(produtoEstoqueId);

        when(produtoEstoqueRepository.findById(produtoEstoqueId)).thenReturn(Optional.empty());

        assertThrows(ProdutoEstoqueException.class, () -> produtoEstoqueServiceImpl.update(produtoEstoqueId, produtoEstoqueDTO));
    }

    @Test
    void testDeleteSuccess() {
        UUID produtoEstoqueId = UUID.randomUUID();
        ProdutoEstoqueDTO produtoEstoqueDTO = createProdutoEstoqueDTO(produtoEstoqueId);
        ProdutoEstoque produtoEstoqueEntity = ProdutoEstoqueMapper.INSTANCE.dtoToEntity(produtoEstoqueDTO);

        when(produtoEstoqueRepository.findById(produtoEstoqueEntity.getUuid())).thenReturn(Optional.of(produtoEstoqueEntity));

        produtoEstoqueServiceImpl.delete(produtoEstoqueDTO.getUuid());

        verify(produtoEstoqueRepository).delete(produtoEstoqueEntity);
    }

    @Test
    void testDeleteNotFound() {
        UUID produtoEstoqueId = UUID.randomUUID();
        when(produtoEstoqueRepository.findById(produtoEstoqueId)).thenReturn(Optional.empty());

        assertThrows(ProdutoEstoqueException.class, () -> produtoEstoqueServiceImpl.delete(produtoEstoqueId));
    }

    private ProdutoEstoqueDTO createProdutoEstoqueDTO(UUID uuid) {
        return ProdutoEstoqueDTO.builder()
                .uuid(uuid)
                .marca("Melitta")
                .quantidadePacote(2)
                .dataEntrega(LocalDate.now())
                .tamanhoPacote("500g")
                .pessoa(createPessoaEntity(UUID.randomUUID()))
                .tipoProduto(createTipoProdutoEntity(UUID.randomUUID()))
                .build();
    }

    private ProdutoEstoque createProdutoEstoqueEntity(UUID uuid) {
        return ProdutoEstoque.builder()
                .uuid(uuid)
                .marca("Melitta")
                .quantidadePacote(2)
                .dataEntrega(LocalDate.now())
                .tamanhoPacote("500g")
                .pessoa(createPessoaEntity(UUID.randomUUID()))
                .tipoProduto(createTipoProdutoEntity(UUID.randomUUID()))
                .build();
    }

    private Pessoa createPessoaEntity(UUID uuid) {
        return Pessoa.builder()
                .uuid(uuid)
                .nome("Rafael")
                .sobrenome("Gabriel")
                .build();
    }

    private TipoProduto createTipoProdutoEntity(UUID uuid) {
        return TipoProduto.builder()
                .uuid(uuid)
                .nome("Café")
                .build();
    }
}
