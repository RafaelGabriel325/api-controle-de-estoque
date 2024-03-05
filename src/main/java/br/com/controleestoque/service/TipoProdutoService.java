package br.com.controleestoque.service;

import br.com.controleestoque.model.dto.TipoProdutoDTO;

import java.util.List;
import java.util.UUID;

public interface TipoProdutoService {
    TipoProdutoDTO findById(UUID id);

    List<TipoProdutoDTO> findAll();

    TipoProdutoDTO create(TipoProdutoDTO tipoProdutoDTO);

    void update(UUID id, TipoProdutoDTO tipoProdutoDTO);

    void delete(UUID id);
}
