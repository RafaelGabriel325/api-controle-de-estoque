package br.com.controleestoque.model.mapper;

import br.com.controleestoque.model.dto.PessoaDTO;
import br.com.controleestoque.model.entity.Pessoa;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PessoaMapper {
    PessoaMapper INSTANCE = Mappers.getMapper(PessoaMapper.class);

    Pessoa dtoToEntity(PessoaDTO pessoaDTO);

    PessoaDTO entityToDto(Pessoa pessoaEntity);
}
