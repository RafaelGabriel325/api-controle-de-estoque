package br.com.controleestoque.model.mapper;

import br.com.controleestoque.model.dto.TipoProdutoDTO;
import br.com.controleestoque.model.entity.TipoProduto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TipoProdutoMapper {
    TipoProdutoMapper INSTANCE = Mappers.getMapper(TipoProdutoMapper.class);

    TipoProduto dtoToEntity(TipoProdutoDTO tipoProdutoDTO);

    TipoProdutoDTO entityToDto(TipoProduto tipoProdutoEntity);
}
