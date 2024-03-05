package br.com.controleestoque.model.mapper;

import br.com.controleestoque.model.dto.ProdutoEstoqueDTO;
import br.com.controleestoque.model.entity.ProdutoEstoque;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProdutoEstoqueMapper {
    ProdutoEstoqueMapper INSTANCE = Mappers.getMapper(ProdutoEstoqueMapper.class);

    ProdutoEstoque dtoToEntity(ProdutoEstoqueDTO produtoEstoqueDTO);

    ProdutoEstoqueDTO entityToDto(ProdutoEstoque produtoEstoqueEntity);
}
