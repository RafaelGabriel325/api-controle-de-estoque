package br.com.controleestoque.service;

import br.com.controleestoque.model.dto.PermissionDTO;

import java.util.List;
import java.util.UUID;

public interface PermissionService {
    PermissionDTO findById(UUID id);

    List<PermissionDTO> findAll();

    PermissionDTO create(PermissionDTO permissionDTO);

    void update(UUID id, PermissionDTO permissionDTO);

    void delete(UUID id);
}
