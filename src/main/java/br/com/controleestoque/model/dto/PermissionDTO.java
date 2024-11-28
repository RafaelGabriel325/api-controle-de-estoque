package br.com.controleestoque.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDTO extends RepresentationModel<PermissionDTO> {
    private UUID uuid;
    @NotBlank
    @Max(100)
    private String description;

}
