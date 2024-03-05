package br.com.controleestoque.model.mapper;

import br.com.controleestoque.model.dto.UserDTO;
import br.com.controleestoque.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User dtoToEntity(UserDTO userDTO);

    UserDTO entityToDto(User user);
}
