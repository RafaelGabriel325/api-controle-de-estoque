package br.com.controleestoque.controller;

import br.com.controleestoque.model.dto.PermissionDTO;
import br.com.controleestoque.service.PermissionService;
import br.com.controleestoque.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static br.com.controleestoque.shared.constant.PathsConstants.*;

@RestController
@RequestMapping(AUTH_BASE)
@Tag(name = "Permissão", description = "Endpoints para Gerenciar as Permissões")
public class PermissionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionController.class);
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping(value = AUTH_PERMISSION_BY_ID,
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Mostra uma permissão",
            description = "Mostra uma permissão pelo ID",
            tags = {"Permissão"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PermissionDTO.class))),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<PermissionDTO> findById(@PathVariable(value = "id") UUID id) {
        LOGGER.info("Finding Permission by ID: {}", id);
        PermissionDTO permissionDTO = this.permissionService.findById(id);
        return ResponseEntity.ok(permissionDTO);
    }

    @GetMapping(value = AUTH_PERMISSION_ALL,
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Mostra uma lista de todas as permissões",
            description = "Mostra uma lista de todas as permissões",
            tags = {"Permissão"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PermissionDTO.class)))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<List<PermissionDTO>> findAll() {
        LOGGER.info("Finding all Permissions");
        List<PermissionDTO> permissionDTOs = this.permissionService.findAll();
        return ResponseEntity.ok(permissionDTOs);
    }

    @PostMapping(value = AUTH_PERMISSION_CREATE,
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Adiciona uma nova permissão",
            description = "Adiciona uma nova permissão",
            tags = {"Permissão"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = PermissionDTO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<PermissionDTO> create(@RequestBody PermissionDTO permissionDTO) {
        LOGGER.info("Creating new Permission");
        PermissionDTO createdPermissionDTO = this.permissionService.create(permissionDTO);
        return ResponseEntity.status(201).body(createdPermissionDTO);
    }

    @PutMapping(value = AUTH_PERMISSION_UPDATE,
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Atualiza uma permissão",
            description = "Atualiza uma permissão pelo ID",
            tags = {"Permissão"},
            responses = {
                    @ApiResponse(description = "Updated", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PermissionDTO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<Void> update(@PathVariable(value = "id") UUID id, @RequestBody PermissionDTO permissionDTO) {
        LOGGER.info("Updating Permission with ID: {}", id);
        this.permissionService.update(id, permissionDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(AUTH_PERMISSION_DELETE)
    @Operation(summary = "Deleta uma permissão",
            description = "Deleta uma permissão pelo ID",
            tags = {"Permissão"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<Void> delete(@PathVariable(value = "id") UUID id) {
        LOGGER.info("Deleting Permission with ID: {}", id);
        this.permissionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}