package br.com.controleestoque.security;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AccountCredentialsDTO implements Serializable {
    private String username;
    private String password;
}
