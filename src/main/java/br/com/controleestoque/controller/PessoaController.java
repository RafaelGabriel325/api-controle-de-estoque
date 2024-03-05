package br.com.controleestoque.controller;

import br.com.controleestoque.model.dto.PessoaDTO;
import br.com.controleestoque.service.PessoaService;
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
@RequestMapping(PESSOA_BASE)
@Tag(name = "Pessoa", description = "Endpoints para Gerenciar Pessoa")
public class PessoaController implements Serializable {
    private final Logger LOGGER = LoggerFactory.getLogger(PessoaController.class);

    private final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @GetMapping(value = PESSOA_BY_ID,
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Mostra uma pessoa",
            description = "Mostra uma pessoa",
            tags = {"Pessoa"},
            responses = {
                    @ApiResponse(description = "Sucess", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PessoaDTO.class))),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Resquet", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<PessoaDTO> findById(@PathVariable(value = "id") UUID id) {
        LOGGER.debug("Request finding a pessoa by id");
        PessoaDTO pessoaDTO = this.pessoaService.findById(id);
        return ResponseEntity.ok().body(pessoaDTO);
    }

    @GetMapping(value = PESSOA_ALL,
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Mostra uma lista de pessoas",
            description = "Mostra uma lista de pessoas",
            tags = {"Pessoa"},
            responses = {
                    @ApiResponse(description = "Sucess", responseCode = "200",
                            content = {@Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PessoaDTO.class))
                            )}),
                    @ApiResponse(description = "Bad Resquet", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<List<PessoaDTO>> findAll() {
        LOGGER.debug("Request all finding a pessoa");
        List<PessoaDTO> pessoaDTOs = this.pessoaService.findAll();
        return ResponseEntity.ok().body(pessoaDTOs);
    }

    @PostMapping(value = PESSOA_CREATE,
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Adiciona uma nova pessoa",
            description = "Adiciona uma nova pessoa, passando um JSON, XML ou YML em represetação da pessoa",
            tags = {"Pessoa"},
            responses = {
                    @ApiResponse(description = "Sucess", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PessoaDTO.class))),
                    @ApiResponse(description = "Bad Resquet", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<PessoaDTO> create(@RequestBody PessoaDTO pessoaDTO) {
        LOGGER.debug("Request create a pessoa");
        PessoaDTO createdPessoaDTO = this.pessoaService.create(pessoaDTO);
        return ResponseEntity.ok().body(createdPessoaDTO);
    }

    @PutMapping(value = PESSOA_UPDATE,
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Atualiza uma pessoa",
            description = "Atualiza uma pessoa, passando um JSON, XML ou YML em represetação da pessoa",
            tags = {"Pessoa"},
            responses = {
                    @ApiResponse(description = "Updated", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PessoaDTO.class))),
                    @ApiResponse(description = "Bad Resquet", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<Void> update(@PathVariable(value = "id") UUID id, @RequestBody PessoaDTO pessoaDTO) {
        LOGGER.debug("Request update a pessoa");
        this.pessoaService.update(id, pessoaDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(PESSOA_DELETE)
    @Operation(summary = "Deleta uma pessoa",
            description = "Deleta uma pessoa, passando um JSON, XML ou YML em represetação da pessoa",
            tags = {"Pessoa"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Resquet", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<Void> delete(@PathVariable(value = "id") UUID id) {
        LOGGER.debug("Request delete a pessoa");
        this.pessoaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
