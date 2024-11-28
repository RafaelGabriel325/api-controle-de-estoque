package br.com.controleestoque.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends RepresentationModel<UserDTO> {
    private UUID uuid;
    @NotBlank
    @Size(max = 20, min = 2)
    private String username;
    @NotBlank
    @Size(max = 100, min = 4)
    private String fullName;
    @NotBlank
    @Size(max = 100)
    private String password;
    @NotBlank
    @Size(max = 1, min = 1)
    private Boolean accountNonExpired;
    @NotBlank
    @Size(max = 1, min = 1)
    private Boolean accountNonLocked;
    @NotBlank
    @Size(max = 1, min = 1)
    private Boolean credentialsNonExpired;
    @NotBlank
    @Size(max = 1, min = 1)
    private Boolean enable;
    @NotBlank
    private List<PermissionDTO> permissions;

}
