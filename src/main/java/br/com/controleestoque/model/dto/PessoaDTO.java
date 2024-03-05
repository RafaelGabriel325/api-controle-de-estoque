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
public class PessoaDTO extends RepresentationModel<PessoaDTO> {
    private UUID uuid;
    @NotBlank
    @Size(max = 25, min = 4)
    private String nome;
    @NotBlank
    @Size(max = 100, min = 4)
    private String sobrenome;

}