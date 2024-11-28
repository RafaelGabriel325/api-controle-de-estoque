package br.com.controleestoque.service.impl;

import br.com.controleestoque.exception.PessoaException;
import br.com.controleestoque.model.dto.PessoaDTO;
import br.com.controleestoque.model.entity.Pessoa;
import br.com.controleestoque.repository.PessoaRepository;
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

class PessoaServiceImplTest {

    private static final UUID PESSOA_ID = UUID.randomUUID();
    private static final String NOME = "Rafael";
    private static final String SOBRENOME = "Gabriel";

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private PessoaServiceImpl pessoaServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByIdSuccess() {
        Pessoa pessoaEntity = createPessoaEntity(PESSOA_ID);

        when(pessoaRepository.findById(PESSOA_ID)).thenReturn(Optional.of(pessoaEntity));

        PessoaDTO result = pessoaServiceImpl.findById(PESSOA_ID);

        assertNotNull(result);
        assertEquals(PESSOA_ID, result.getUuid());
        assertEquals(NOME, result.getNome());
        assertEquals(SOBRENOME, result.getSobrenome());
    }

    @Test
    void testFindByIdNotFound() {
        when(pessoaRepository.findById(PESSOA_ID)).thenReturn(Optional.empty());

        assertThrows(PessoaException.class, () -> pessoaServiceImpl.findById(PESSOA_ID));
    }

    @Test
    void testFindAllSuccess() {
        Pessoa pessoaEntity = createPessoaEntity(PESSOA_ID);
        List<Pessoa> pessoaList = Collections.singletonList(pessoaEntity);

        when(pessoaRepository.findAll()).thenReturn(pessoaList);

        List<PessoaDTO> result = pessoaServiceImpl.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(PESSOA_ID, result.get(0).getUuid());
        assertEquals(NOME, result.get(0).getNome());
        assertEquals(SOBRENOME, result.get(0).getSobrenome());
    }

    @Test
    void testFindAllEmptyList() {
        when(pessoaRepository.findAll()).thenReturn(Collections.emptyList());

        List<PessoaDTO> result = pessoaServiceImpl.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateSuccess() {
        PessoaDTO pessoaDTO = createPessoaDTO(PESSOA_ID);
        Pessoa pessoaEntity = createPessoaEntity(PESSOA_ID);

        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoaEntity);

        PessoaDTO result = pessoaServiceImpl.create(pessoaDTO);

        assertNotNull(result);
        assertEquals(PESSOA_ID, result.getUuid());
        assertEquals(NOME, result.getNome());
        assertEquals(SOBRENOME, result.getSobrenome());
    }

    @Test
    void testUpdateSuccess() {
        PessoaDTO pessoaDTO = createPessoaDTO(PESSOA_ID);
        Pessoa pessoaEntity = createPessoaEntity(PESSOA_ID);

        when(pessoaRepository.findById(PESSOA_ID)).thenReturn(Optional.of(pessoaEntity));

        pessoaServiceImpl.update(PESSOA_ID, pessoaDTO);

        verify(pessoaRepository).save(pessoaEntity);
        assertEquals(NOME, pessoaEntity.getNome());
        assertEquals(SOBRENOME, pessoaEntity.getSobrenome());
    }

    @Test
    void testUpdateNotFound() {
        PessoaDTO pessoaDTO = createPessoaDTO(PESSOA_ID);

        when(pessoaRepository.findById(PESSOA_ID)).thenReturn(Optional.empty());

        assertThrows(PessoaException.class, () -> pessoaServiceImpl.update(PESSOA_ID, pessoaDTO));
    }

    @Test
    void testDeleteSuccess() {
        Pessoa pessoaEntity = createPessoaEntity(PESSOA_ID);

        when(pessoaRepository.findById(PESSOA_ID)).thenReturn(Optional.of(pessoaEntity));

        pessoaServiceImpl.delete(PESSOA_ID);

        verify(pessoaRepository).delete(pessoaEntity);
    }

    @Test
    void testDeleteNotFound() {
        when(pessoaRepository.findById(PESSOA_ID)).thenReturn(Optional.empty());

        assertThrows(PessoaException.class, () -> pessoaServiceImpl.delete(PESSOA_ID));
    }

    private PessoaDTO createPessoaDTO(UUID uuid) {
        return PessoaDTO.builder()
                .uuid(uuid)
                .nome(NOME)
                .sobrenome(SOBRENOME)
                .build();
    }

    private Pessoa createPessoaEntity(UUID uuid) {
        return Pessoa.builder()
                .uuid(uuid)
                .nome(NOME)
                .sobrenome(SOBRENOME)
                .build();
    }
}