package br.com.controleestoque.controller;

import br.com.controleestoque.model.dto.ProdutoEstoqueDTO;
import br.com.controleestoque.model.dto.TipoProdutoDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import static br.com.controleestoque.shared.constant.PathsConstants.*;

@RestController
@RequestMapping(PRODUTO_BASE)
@Tag(name = "Produto em Estoque", description = "Endpoints para Gerenciar Produto em Estoque")
public class ProdutoEstoqueController implements Serializable {
    private final Logger LOGGER = LoggerFactory.getLogger(ProdutoEstoqueController.class);

    private final ProdutoEstoqueService produtoEstoqueService;

    public ProdutoEstoqueController(ProdutoEstoqueService produtoEstoqueService) {
        this.produtoEstoqueService = produtoEstoqueService;
    }

    @GetMapping(value = PRODUTO_BY_ID,
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Mostra um produto que há em estoque",
            description = "Mostra um produto que há em estoque",
            tags = {"Produto em Estoque"},
            responses = {
                    @ApiResponse(description = "Sucess", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ProdutoEstoqueDTO.class))),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Resquet", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<ProdutoEstoqueDTO> findById(@PathVariable(value = "id") UUID id) {
        LOGGER.debug("Request finding an produto by id");
        ProdutoEstoqueDTO ProdutoEstoqueDTO = this.produtoEstoqueService.findById(id);
        return ResponseEntity.ok().body(ProdutoEstoqueDTO);
    }

    @GetMapping(value = PRODUTO_ALL,
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Mostra uma lista de todos os produto que há em estoque",
            description = "Mostra uma lista de todos os produto que há em estoque",
            tags = {"Produto em Estoque"},
            responses = {
                    @ApiResponse(description = "Sucess", responseCode = "200",
                            content = {@Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ProdutoEstoqueDTO.class))
                            )}),
                    @ApiResponse(description = "Bad Resquet", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<List<ProdutoEstoqueDTO>> findAll() {
        LOGGER.debug("Request all finding an produto");
        List<ProdutoEstoqueDTO> produtoEstoqueDTOS = this.produtoEstoqueService.findAll();
        return ResponseEntity.ok().body(produtoEstoqueDTOS);
    }

    @PostMapping(value = PRODUTO_CREATE,
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Adiciona um novo produto para estoque",
            description = "Adiciona um novo produto para estoque, passando um JSON, XML ou YML em represetação da pessoa",
            tags = {"Produto em Estoque"},
            responses = {
                    @ApiResponse(description = "Sucess", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TipoProdutoDTO.class))),
                    @ApiResponse(description = "Bad Resquet", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<ProdutoEstoqueDTO> create(@RequestBody ProdutoEstoqueDTO produtoEstoqueDTO){
        LOGGER.debug("Request create an produto");
        ProdutoEstoqueDTO createdProdutoEstoqueDTO = this.produtoEstoqueService.create(produtoEstoqueDTO);
        return ResponseEntity.ok().body(createdProdutoEstoqueDTO);
    }

    @PutMapping(value = PRODUTO_UPDATE,
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Atualiza um produto que tem no estoque",
            description = "Atualiza um produto que tem no estoque, passando um JSON, XML ou YML em represetação da pessoa",
            tags = {"Produto em Estoque"},
            responses = {
                    @ApiResponse(description = "Updated", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TipoProdutoDTO.class))),
                    @ApiResponse(description = "Bad Resquet", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<Void> update(@PathVariable(value = "id") UUID id, @RequestBody ProdutoEstoqueDTO ProdutoEstoqueDTO) {
        LOGGER.debug("Request update an produto");
        this.produtoEstoqueService.update(id, ProdutoEstoqueDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = PRODUTO_DELETE)
    @Operation(summary = "Deleta um produto que está no estoque",
            description = "Deleta um produto que está no estoque, passando um JSON, XML ou YML em represetação da pessoa",
            tags = {"Produto em Estoque"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Resquet", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<Void> delete(@PathVariable(value = "id") UUID id) {
        LOGGER.debug("Request delete an produto");
        this.produtoEstoqueService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
