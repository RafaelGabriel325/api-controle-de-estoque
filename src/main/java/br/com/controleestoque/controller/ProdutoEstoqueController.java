package br.com.controleestoque.controller;

import br.com.controleestoque.model.dto.ProdutoEstoqueDTO;
import br.com.controleestoque.service.ProdutoEstoqueService;
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
@RequestMapping(PRODUTO_BASE)
@Tag(name = "Produto em Estoque", description = "Endpoints para Gerenciar Produto em Estoque")
public class ProdutoEstoqueController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoEstoqueController.class);
    private final ProdutoEstoqueService produtoEstoqueService;

    public ProdutoEstoqueController(ProdutoEstoqueService produtoEstoqueService) {
        this.produtoEstoqueService = produtoEstoqueService;
    }

    @GetMapping(value = PRODUTO_BY_ID,
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Mostra um produto que há em estoque",
            description = "Mostra um produto que há em estoque pelo ID",
            tags = {"Produto em Estoque"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ProdutoEstoqueDTO.class))),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<ProdutoEstoqueDTO> findById(@PathVariable(value = "id") UUID id) {
        LOGGER.info("Finding produto by ID: {}", id);
        ProdutoEstoqueDTO produtoEstoqueDTO = this.produtoEstoqueService.findById(id);
        return ResponseEntity.ok(produtoEstoqueDTO);
    }

    @GetMapping(value = PRODUTO_ALL,
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Mostra uma lista de todos os produtos que há em estoque",
            description = "Mostra uma lista de todos os produtos que há em estoque",
            tags = {"Produto em Estoque"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProdutoEstoqueDTO.class)))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<List<ProdutoEstoqueDTO>> findAll() {
        LOGGER.info("Finding all produtos");
        List<ProdutoEstoqueDTO> produtoEstoqueDTOS = this.produtoEstoqueService.findAll();
        return ResponseEntity.ok(produtoEstoqueDTOS);
    }

    @PostMapping(value = PRODUTO_CREATE,
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Adiciona um novo produto para estoque",
            description = "Adiciona um novo produto para estoque",
            tags = {"Produto em Estoque"},
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = ProdutoEstoqueDTO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<ProdutoEstoqueDTO> create(@RequestBody ProdutoEstoqueDTO produtoEstoqueDTO) {
        LOGGER.info("Creating new produto");
        ProdutoEstoqueDTO createdProdutoEstoqueDTO = this.produtoEstoqueService.create(produtoEstoqueDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProdutoEstoqueDTO);
    }

    @PutMapping(value = PRODUTO_UPDATE,
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Atualiza um produto que tem no estoque",
            description = "Atualiza um produto que tem no estoque pelo ID",
            tags = {"Produto em Estoque"},
            responses = {
                    @ApiResponse(description = "Updated", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ProdutoEstoqueDTO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<Void> update(@PathVariable(value = "id") UUID id, @RequestBody ProdutoEstoqueDTO produtoEstoqueDTO) {
        LOGGER.info("Updating produto with ID: {}", id);
        this.produtoEstoqueService.update(id, produtoEstoqueDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = PRODUTO_DELETE)
    @Operation(summary = "Deleta um produto que está no estoque",
            description = "Deleta um produto que está no estoque pelo ID",
            tags = {"Produto em Estoque"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<Void> delete(@PathVariable(value = "id") UUID id) {
        LOGGER.info("Deleting produto with ID: {}", id);
        this.produtoEstoqueService.delete(id);
        return ResponseEntity.noContent().build();
    }
}