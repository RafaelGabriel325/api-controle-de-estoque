package br.com.controleestoque.security;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountCredentialsDTO implements Serializable {
    private String username;
    private String password;
}
