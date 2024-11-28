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
    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionServiceImpl.class);
    private final PermissionRepository permissionRepository;
    private static final PermissionMapper mapper = PermissionMapper.INSTANCE;

    @Override
    public PermissionDTO findById(UUID id) {
        LOGGER.info("Finding Permission by ID: {}", id);
        Permission permissionEntity = findPermissionById(id);
        return addHateoasLinks(mapper.entityToDto(permissionEntity));
    }

    @Override
    public List<PermissionDTO> findAll() {
        LOGGER.info("Finding all Permissions");
        return permissionRepository.findAll()
                .stream()
                .map(mapper::entityToDto)
                .map(this::addHateoasLinks)
                .toList();
    }

    @Override
    public PermissionDTO create(PermissionDTO permissionDTO) {
        LOGGER.info("Creating a new Permission");
        Permission permissionEntity = mapper.dtoToEntity(permissionDTO);
        permissionRepository.save(permissionEntity);
        return addHateoasLinks(mapper.entityToDto(permissionEntity));
    }

    @Override
    public void update(UUID id, PermissionDTO permissionDTO) {
        LOGGER.info("Updating Permission with ID: {}", id);
        Permission permissionEntity = findPermissionById(id);
        permissionEntity.setDescription(permissionDTO.getDescription());
        permissionRepository.save(permissionEntity);
    }

    @Override
    public void delete(UUID id) {
        LOGGER.info("Deleting Permission with ID: {}", id);
        Permission permissionEntity = findPermissionById(id);
        permissionRepository.delete(permissionEntity);
    }

    private Permission findPermissionById(UUID id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Permission not found with ID: {}", id);
                    return new PermissionException("Permission not found with ID: " + id);
                });
    }

    private PermissionDTO addHateoasLinks(PermissionDTO permissionDTO) {
        permissionDTO.add(linkTo(methodOn(PermissionController.class).findById(permissionDTO.getUuid())).withSelfRel());
        return permissionDTO;
    }
}