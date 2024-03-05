package br.com.controleestoque.model.mapper;

import br.com.controleestoque.model.dto.PermissionDTO;
import br.com.controleestoque.model.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PermissionMapper {
    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);

    Permission dtoToEntity(PermissionDTO permissionDTO);

    PermissionDTO entityToDto(Permission permission);
}
