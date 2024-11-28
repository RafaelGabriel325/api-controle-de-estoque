package br.com.controleestoque.service.impl;

import br.com.controleestoque.exception.ProdutoEstoqueException;
import br.com.controleestoque.model.dto.ProdutoEstoqueDTO;
import br.com.controleestoque.model.entity.Pessoa;
import br.com.controleestoque.model.entity.ProdutoEstoque;
import br.com.controleestoque.model.entity.TipoProduto;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProdutoEstoqueServiceImplTest {

    private static final UUID PRODUTO_ESTOQUE_ID = UUID.randomUUID();
    private static final UUID PESSOA_ID = UUID.randomUUID();
    private static final UUID TIPO_PRODUTO_ID = UUID.randomUUID();
    private static final String MARCA = "Melitta";
    private static final int QUANTIDADE_PACOTE = 2;
    private static final String TAMANHO_PACOTE = "500g";
    private static final LocalDate DATA_ENTREGA = LocalDate.now();

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
        ProdutoEstoque produtoEstoqueEntity = createProdutoEstoqueEntity(PRODUTO_ESTOQUE_ID);

        when(produtoEstoqueRepository.findById(PRODUTO_ESTOQUE_ID)).thenReturn(Optional.of(produtoEstoqueEntity));

        ProdutoEstoqueDTO result = produtoEstoqueServiceImpl.findById(PRODUTO_ESTOQUE_ID);

        assertNotNull(result);
        assertEquals(PRODUTO_ESTOQUE_ID, result.getUuid());
        assertEquals(MARCA, result.getMarca());
        assertEquals(QUANTIDADE_PACOTE, result.getQuantidadePacote());
    }

    @Test
    void testFindByIdNotFound() {
        when(produtoEstoqueRepository.findById(PRODUTO_ESTOQUE_ID)).thenReturn(Optional.empty());

        assertThrows(ProdutoEstoqueException.class, () -> produtoEstoqueServiceImpl.findById(PRODUTO_ESTOQUE_ID));
    }

    @Test
    void testFindAllSuccess() {
        List<ProdutoEstoque> produtoEstoqueList = Collections.singletonList(createProdutoEstoqueEntity(PRODUTO_ESTOQUE_ID));

        when(produtoEstoqueRepository.findAll()).thenReturn(produtoEstoqueList);

        List<ProdutoEstoqueDTO> result = produtoEstoqueServiceImpl.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(PRODUTO_ESTOQUE_ID, result.get(0).getUuid());
        assertEquals(MARCA, result.get(0).getMarca());
        assertEquals(QUANTIDADE_PACOTE, result.get(0).getQuantidadePacote());
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
        ProdutoEstoqueDTO produtoEstoqueDTO = createProdutoEstoqueDTO(PRODUTO_ESTOQUE_ID);
        ProdutoEstoque produtoEstoqueEntity = createProdutoEstoqueEntity(PRODUTO_ESTOQUE_ID);

        Pessoa pessoaEntity = createPessoaEntity(PESSOA_ID);
        when(pessoaRepository.findById(PESSOA_ID)).thenReturn(Optional.of(pessoaEntity));

        TipoProduto tipoProdutoEntity = createTipoProdutoEntity(TIPO_PRODUTO_ID);
        when(tipoProdutoRepository.findById(TIPO_PRODUTO_ID)).thenReturn(Optional.of(tipoProdutoEntity));

        when(produtoEstoqueRepository.save(any(ProdutoEstoque.class))).thenReturn(produtoEstoqueEntity);

        ProdutoEstoqueDTO result = produtoEstoqueServiceImpl.create(produtoEstoqueDTO);

        assertNotNull(result);
        assertEquals(PRODUTO_ESTOQUE_ID, result.getUuid());
        assertEquals(MARCA, result.getMarca());
        assertEquals(QUANTIDADE_PACOTE, result.getQuantidadePacote());
    }

    @Test
    void testUpdateProdutoEstoqueSuccess() {
        ProdutoEstoqueDTO produtoEstoqueDTO = createProdutoEstoqueDTO(PRODUTO_ESTOQUE_ID);
        ProdutoEstoque produtoEstoqueEntity = createProdutoEstoqueEntity(PRODUTO_ESTOQUE_ID);

        when(produtoEstoqueRepository.findById(PRODUTO_ESTOQUE_ID)).thenReturn(Optional.of(produtoEstoqueEntity));

        Pessoa pessoaEntity = createPessoaEntity(PESSOA_ID);
        when(pessoaRepository.findById(PESSOA_ID)).thenReturn(Optional.of(pessoaEntity));

        TipoProduto tipoProdutoEntity = createTipoProdutoEntity(TIPO_PRODUTO_ID);
        when(tipoProdutoRepository.findById(TIPO_PRODUTO_ID)).thenReturn(Optional.of(tipoProdutoEntity));

        produtoEstoqueServiceImpl.update(PRODUTO_ESTOQUE_ID, produtoEstoqueDTO);

        verify(produtoEstoqueRepository).save(produtoEstoqueEntity);
        assertEquals(MARCA, produtoEstoqueEntity.getMarca());
        assertEquals(QUANTIDADE_PACOTE, produtoEstoqueEntity.getQuantidadePacote());
    }

    @Test
    void testUpdateNotFound() {
        ProdutoEstoqueDTO produtoEstoqueDTO = createProdutoEstoqueDTO(PRODUTO_ESTOQUE_ID);

        when(produtoEstoqueRepository.findById(PRODUTO_ESTOQUE_ID)).thenReturn(Optional.empty());

        assertThrows(ProdutoEstoqueException.class, () -> produtoEstoqueServiceImpl.update(PRODUTO_ESTOQUE_ID, produtoEstoqueDTO));
    }

    @Test
    void testDeleteSuccess() {
        ProdutoEstoque produtoEstoqueEntity = createProdutoEstoqueEntity(PRODUTO_ESTOQUE_ID);

        when(produtoEstoqueRepository.findById(PRODUTO_ESTOQUE_ID)).thenReturn(Optional.of(produtoEstoqueEntity));

        produtoEstoqueServiceImpl.delete(PRODUTO_ESTOQUE_ID);

        verify(produtoEstoqueRepository).delete(produtoEstoqueEntity);
    }

    @Test
    void testDeleteNotFound() {
        when(produtoEstoqueRepository.findById(PRODUTO_ESTOQUE_ID)).thenReturn(Optional.empty());

        assertThrows(ProdutoEstoqueException.class, () -> produtoEstoqueServiceImpl.delete(PRODUTO_ESTOQUE_ID));
    }

    private ProdutoEstoqueDTO createProdutoEstoqueDTO(UUID uuid) {
        return ProdutoEstoqueDTO.builder()
                .uuid(uuid)
                .marca(MARCA)
                .quantidadePacote(QUANTIDADE_PACOTE)
                .dataEntrega(DATA_ENTREGA)
                .tamanhoPacote(TAMANHO_PACOTE)
                .pessoa(createPessoaDTO(PESSOA_ID))
                .tipoProduto(createTipoProdutoDTO(TIPO_PRODUTO_ID))
                .build();
    }

    private ProdutoEstoque createProdutoEstoqueEntity(UUID uuid) {
        return ProdutoEstoque.builder()
                .uuid(uuid)
                .marca(MARCA)
                .quantidadePacote(QUANTIDADE_PACOTE)
                .dataEntrega(DATA_ENTREGA)
                .tamanhoPacote(TAMANHO_PACOTE)
                .pessoa(createPessoaEntity(PESSOA_ID))
                .tipoProduto(createTipoProdutoEntity(TIPO_PRODUTO_ID))
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

    private Pessoa createPessoaDTO(UUID uuid) {
        return Pessoa.builder()
                .uuid(uuid)
                .nome("Rafael")
                .sobrenome("Gabriel")
                .build();
    }

    private TipoProduto createTipoProdutoDTO(UUID uuid) {
        return TipoProduto.builder()
                .uuid(uuid)
                .nome("Café")
                .build();
    }
}