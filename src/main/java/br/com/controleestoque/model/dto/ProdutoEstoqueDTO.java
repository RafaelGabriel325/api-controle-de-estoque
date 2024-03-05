package br.com.controleestoque.model.dto;

import br.com.controleestoque.model.entity.Pessoa;
import br.com.controleestoque.model.entity.TipoProduto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ProdutoEstoqueDTO extends RepresentationModel<ProdutoEstoqueDTO> {
    private UUID uuid;
    @NotBlank
    @Size(max = 25, min = 4)
    private String marca;
    @NotBlank
    @Size(min = 1)
    private Integer quantidadePacote;
    @NotBlank
    private LocalDate dataEntrega;
    @Size(max = 5, min = 3)
    private String tamanhoPacote;
    @NotBlank
    private TipoProduto tipoProduto;
    @NotBlank
    private Pessoa pessoa;
}