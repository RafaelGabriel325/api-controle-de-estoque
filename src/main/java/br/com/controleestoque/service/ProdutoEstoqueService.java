package br.com.controleestoque.service;

import br.com.controleestoque.model.dto.ProdutoEstoqueDTO;

import java.util.List;
import java.util.UUID;

public interface ProdutoEstoqueService {
    ProdutoEstoqueDTO findById(UUID id);

    List<ProdutoEstoqueDTO> findAll();

    ProdutoEstoqueDTO create(ProdutoEstoqueDTO produtoEstoqueDTO);

    void update(UUID id, ProdutoEstoqueDTO produtoEstoqueDTO);

    void delete(UUID id);
}
