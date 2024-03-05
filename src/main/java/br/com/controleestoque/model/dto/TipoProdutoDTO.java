package br.com.controleestoque.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TipoProdutoDTO extends RepresentationModel<TipoProdutoDTO> {
    private UUID uuid;
    @NotBlank
    @Size(max = 6, min = 4)
    private String nome;

}