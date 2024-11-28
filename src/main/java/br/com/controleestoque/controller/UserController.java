package br.com.controleestoque.controller;

import br.com.controleestoque.model.dto.UserDTO;
import br.com.controleestoque.service.UserService;
import br.com.controleestoque.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static br.com.controleestoque.shared.constant.PathsConstants.*;

@RestController
@RequestMapping(AUTH_BASE)
@Tag(name = "User", description = "Endpoints para Gerenciar os Usuários")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = AUTH_BY_ID,
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Mostra um usuário",
            description = "Mostra um usuário pelo ID",
            tags = {"User"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<UserDTO> findById(@PathVariable(value = "id") UUID id) {
        LOGGER.info("Finding user by ID: {}", id);
        UserDTO userDTO = this.userService.findById(id);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping(value = AUTH_ALL,
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Mostra uma lista de usuários",
            description = "Mostra uma lista de todos os usuários",
            tags = {"User"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<List<UserDTO>> findAll() {
        LOGGER.info("Finding all users");
        List<UserDTO> userDTOList = this.userService.findAll();
        return ResponseEntity.ok(userDTOList);
    }

    @PostMapping(value = AUTH_CREATE,
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Adiciona um novo usuário",
            description = "Adiciona um novo usuário",
            tags = {"User"},
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO userDTO) {
        LOGGER.info("Creating new user");
        UserDTO createdUserDTO = this.userService.create(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDTO);
    }

    @PutMapping(value = AUTH_UPDATE,
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Atualiza um usuário",
            description = "Atualiza um usuário pelo ID",
            tags = {"User"},
            responses = {
                    @ApiResponse(description = "Updated", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<Void> update(@PathVariable(value = "id") UUID id, @RequestBody UserDTO userDTO) {
        LOGGER.info("Updating user with ID: {}", id);
        this.userService.update(id, userDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(AUTH_DELETE)
    @Operation(summary = "Deleta um usuário",
            description = "Deleta um usuário pelo ID",
            tags = {"User"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<Void> delete(@PathVariable(value = "id") UUID id) {
        LOGGER.info("Deleting user with ID: {}", id);
        this.userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}