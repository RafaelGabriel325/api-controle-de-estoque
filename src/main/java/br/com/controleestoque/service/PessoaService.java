package br.com.controleestoque.service;

import br.com.controleestoque.model.dto.PessoaDTO;

import java.util.List;
import java.util.UUID;

public interface PessoaService {
    PessoaDTO findById(UUID id);

    List<PessoaDTO> findAll();

    PessoaDTO create(PessoaDTO pessoaDTO);

    void update(UUID id, PessoaDTO pessoaDTO);

    void delete(UUID id);
}
