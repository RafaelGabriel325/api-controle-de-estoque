package br.com.controleestoque.controller;

import br.com.controleestoque.model.dto.TipoProdutoDTO;
import br.com.controleestoque.service.TipoProdutoService;
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
@RequestMapping(TIPO_PRODUTO_BASE)
@Tag(name = "Tipo de Produto", description = "Endpoints para Gerenciar Tipo de Produto")
public class TipoProdutoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TipoProdutoController.class);
    private final TipoProdutoService tipoProdutoService;

    public TipoProdutoController(TipoProdutoService tipoProdutoService) {
        this.tipoProdutoService = tipoProdutoService;
    }

    @GetMapping(value = TIPO_PRODUTO_BY_ID,
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Mostra um tipo de produto",
            description = "Mostra um tipo de produto pelo ID",
            tags = {"Tipo de Produto"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TipoProdutoDTO.class))),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<TipoProdutoDTO> findById(@PathVariable(value = "id") UUID id) {
        LOGGER.info("Finding tipo produto by ID: {}", id);
        TipoProdutoDTO tipoProdutoDTO = this.tipoProdutoService.findById(id);
        return ResponseEntity.ok(tipoProdutoDTO);
    }

    @GetMapping(value = TIPO_PRODUTO_ALL,
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Mostra uma lista de todos os tipos de produto",
            description = "Mostra uma lista de todos os tipos de produto",
            tags = {"Tipo de Produto"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TipoProdutoDTO.class)))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<List<TipoProdutoDTO>> findAll() {
        LOGGER.info("Finding all tipos de produto");
        List<TipoProdutoDTO> tipoProdutoDTOs = this.tipoProdutoService.findAll();
        return ResponseEntity.ok(tipoProdutoDTOs);
    }

    @PostMapping(value = TIPO_PRODUTO_CREATE,
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Adiciona um novo tipo de produto",
            description = "Adiciona um novo tipo de produto",
            tags = {"Tipo de Produto"},
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = TipoProdutoDTO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<TipoProdutoDTO> create(@RequestBody TipoProdutoDTO tipoProdutoDTO) {
        LOGGER.info("Creating new tipo produto");
        TipoProdutoDTO createdTipoProdutoDTO = this.tipoProdutoService.create(tipoProdutoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTipoProdutoDTO);
    }

    @PutMapping(value = TIPO_PRODUTO_UPDATE,
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Atualiza um tipo de produto",
            description = "Atualiza um tipo de produto pelo ID",
            tags = {"Tipo de Produto"},
            responses = {
                    @ApiResponse(description = "Updated", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TipoProdutoDTO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<Void> update(@PathVariable(value = "id") UUID id, @RequestBody TipoProdutoDTO tipoProdutoDTO) {
        LOGGER.info("Updating tipo produto with ID: {}", id);
        this.tipoProdutoService.update(id, tipoProdutoDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(TIPO_PRODUTO_DELETE)
    @Operation(summary = "Deleta um tipo de produto",
            description = "Deleta um tipo de produto pelo ID",
            tags = {"Tipo de Produto"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<Void> delete(@PathVariable(value = "id") UUID id) {
        LOGGER.info("Deleting tipo produto with ID: {}", id);
        this.tipoProdutoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}