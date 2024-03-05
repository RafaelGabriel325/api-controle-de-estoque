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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PessoaServiceImplTest {

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
        UUID pessoaId = UUID.randomUUID();
        Pessoa pessoaEntity = createPessoaEntity(pessoaId);

        when(pessoaRepository.findById(pessoaEntity.getUuid())).thenReturn(Optional.of(pessoaEntity));

        PessoaDTO result = pessoaServiceImpl.findById(pessoaId);

        assertNotNull(result);
        assertEquals(pessoaEntity.getUuid(), result.getUuid());
        assertEquals(pessoaEntity.getNome(), result.getNome());
        assertEquals(pessoaEntity.getSobrenome(), result.getSobrenome());
    }

    @Test
    void testFindByIdNotFound() {
        UUID pessoaId = UUID.randomUUID();
        when(pessoaRepository.findById(pessoaId)).thenReturn(Optional.empty());

        assertThrows(PessoaException.class, () -> pessoaServiceImpl.findById(pessoaId));
    }

    @Test
    void testFindAllSuccess() {
        List<Pessoa> pessoaList = new ArrayList<>();

        UUID pessoaId = UUID.randomUUID();
        Pessoa pessoaEntity = createPessoaEntity(pessoaId);

        pessoaList.add(pessoaEntity);

        when(pessoaRepository.findAll()).thenReturn(pessoaList);

        List<PessoaDTO> result = pessoaServiceImpl.findAll();

        assertNotNull(result);
        assertEquals(pessoaList.get(0).getUuid(), result.get(0).getUuid());
        assertEquals(pessoaList.get(0).getNome(), result.get(0).getNome());
        assertEquals(pessoaList.get(0).getSobrenome(), result.get(0).getSobrenome());
        assertEquals(pessoaList.size(), result.size());
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
        PessoaDTO pessoaDTO = createPessoaDTO(UUID.randomUUID());
        Pessoa pessoaEntity = createPessoaEntity(pessoaDTO.getUuid());

        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoaEntity);

        PessoaDTO result = pessoaServiceImpl.create(pessoaDTO);

        assertNotNull(result);
        assertNotNull(result.getUuid());
        assertEquals(pessoaDTO.getNome(), result.getNome());
        assertEquals(pessoaDTO.getSobrenome(), result.getSobrenome());
    }

    @Test
    public void testUpdatePessoaSucces() {
        UUID id = UUID.randomUUID();

        PessoaDTO pessoaDTO = createPessoaDTO(id);
        Pessoa pessoaEntity = createPessoaEntity(id);

        when(pessoaRepository.findById(pessoaEntity.getUuid())).thenReturn(Optional.of(pessoaEntity));
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoaEntity);

        pessoaServiceImpl.update(id, pessoaDTO);

        assertEquals(pessoaDTO.getNome(), pessoaEntity.getNome());
        assertEquals(pessoaDTO.getSobrenome(), pessoaEntity.getSobrenome());
    }

    @Test
    void testUpdateNotFound() {
        UUID pessoaId = UUID.randomUUID();

        PessoaDTO pessoaDTO = createPessoaDTO(pessoaId);

        when(pessoaRepository.findById(pessoaId)).thenReturn(Optional.empty());

        assertThrows(PessoaException.class, () -> pessoaServiceImpl.update(pessoaId, pessoaDTO));
    }

    @Test
    void testDeleteSuccess() {
        UUID pessoaId = UUID.randomUUID();
        PessoaDTO pessoaDTO = createPessoaDTO(pessoaId);
        Pessoa pessoaEntity = createPessoaEntity(pessoaId);

        when(pessoaRepository.findById(pessoaEntity.getUuid())).thenReturn(Optional.of(pessoaEntity));

        pessoaServiceImpl.delete(pessoaDTO.getUuid());

        verify(pessoaRepository).delete(pessoaEntity);
    }

    @Test
    void testDeleteNotFound() {
        UUID pessoaId = UUID.randomUUID();
        when(pessoaRepository.findById(pessoaId)).thenReturn(Optional.empty());

        assertThrows(PessoaException.class, () -> pessoaServiceImpl.delete(pessoaId));
    }

    private PessoaDTO createPessoaDTO(UUID uuid) {
        return PessoaDTO.builder()
                .uuid(uuid)
                .nome("Rafael")
                .sobrenome("Gabriel")
                .build();
    }

    private Pessoa createPessoaEntity(UUID uuid) {
        return Pessoa.builder()
                .uuid(uuid)
                .nome("Rafael")
                .sobrenome("Gabriel")
                .build();
    }
}
