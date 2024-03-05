package br.com.controleestoque.service;

import br.com.controleestoque.security.AccountCredentialsDTO;
import br.com.controleestoque.security.TokenDTO;

public interface AuthService {
    TokenDTO signIn(AccountCredentialsDTO accountCredentialsDTO);

    TokenDTO refreshToken(String username, String refreshToken);
}
