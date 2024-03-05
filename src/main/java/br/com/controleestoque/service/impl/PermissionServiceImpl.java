package br.com.controleestoque.service.impl;

import br.com.controleestoque.controller.PermissionController;
import br.com.controleestoque.exception.PermissionException;
import br.com.controleestoque.model.dto.PermissionDTO;
import br.com.controleestoque.model.entity.Permission;
import br.com.controleestoque.model.mapper.PermissionMapper;
import br.com.controleestoque.repository.PermissionRepository;
import br.com.controleestoque.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final Logger LOGGER = LoggerFactory.getLogger(PermissionServiceImpl.class);
    private final PermissionRepository permissionRepository;

    @Override
    public PermissionDTO findById(UUID id) {
        LOGGER.info("Finding a Permission by id");
        Permission permissionEntity = this.permissionRepository
                .findById(id)
                .orElseThrow(() -> new PermissionException("Permission not found with ID: " + id));
        PermissionDTO permissionDTO = PermissionMapper.INSTANCE.entityToDto(permissionEntity);
        permissionDTO.add(linkTo(methodOn(PermissionController.class).findById(id)).withSelfRel());
        return permissionDTO;
    }

    @Override
    public List<PermissionDTO> findAll() {
        LOGGER.info("Finding all Permission");
        List<PermissionDTO> permissionDTOList = this.permissionRepository.findAll().stream().map(PermissionMapper.INSTANCE::entityToDto).toList();
        permissionDTOList.forEach(permission -> permission.add(linkTo(methodOn(PermissionController.class).findById(permission.getUuid())).
                withSelfRel()));
        return permissionDTOList;
    }

    @Override
    public PermissionDTO create(PermissionDTO permissionDTO) {
        LOGGER.info("Creating a Permission");
        Permission permissionEntity = PermissionMapper.INSTANCE.dtoToEntity(permissionDTO);
        this.permissionRepository.save(permissionEntity);
        PermissionDTO createdPermissionDTO = PermissionMapper.INSTANCE.entityToDto(permissionEntity);
        createdPermissionDTO.add(linkTo(methodOn(PermissionController.class).findById(createdPermissionDTO.getUuid())).withSelfRel());
        return createdPermissionDTO;
    }

    @Override
    public void update(UUID id, PermissionDTO permissionDTO) {
        LOGGER.info("Updating a Permission");
        Permission permissionEntity = this.permissionRepository
                .findById(id)
                .orElseThrow(() -> new PermissionException("Permission not found with ID: " + id));
        permissionEntity.setDescription(permissionDTO.getDescription());
        permissionRepository.save(permissionEntity);
    }

    @Override
    public void delete(UUID id) {
        LOGGER.info("Deleting a Permission");
        Permission permissionEntity = this.permissionRepository
                .findById(id)
                .orElseThrow(() -> new PermissionException("Permission not found with ID: " + id));
        this.permissionRepository.delete(permissionEntity);
    }
}
