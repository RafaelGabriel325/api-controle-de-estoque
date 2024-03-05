package br.com.controleestoque.service.impl;

import br.com.controleestoque.model.entity.User;
import br.com.controleestoque.repository.UserRepository;
import br.com.controleestoque.security.AccountCredentialsDTO;
import br.com.controleestoque.security.TokenDTO;
import br.com.controleestoque.security.jwt.JwtTokenProvider;
import br.com.controleestoque.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final Logger LOGGER = LoggerFactory.getLogger(PessoaServiceImpl.class);
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenDTO signIn(AccountCredentialsDTO accountCredentialsDTO) {
        try {
            LOGGER.info("Request sign in");
            String username = accountCredentialsDTO.getUsername();
            String password = accountCredentialsDTO.getPassword();

            User user = this.userRepository.findByUsername(username);

            if (user != null) {
                if (passwordEncoder.matches(password, user.getPassword())) {
                    return this.jwtTokenProvider.createAccessToken(username, user.getPermissions());
                } else {
                    throw new BadCredentialsException("Invalid username/password supplied!");
                }
            } else {
                throw new UsernameNotFoundException("Username: " + username + " not found");
            }

        } catch (BadCredentialsException | UsernameNotFoundException exception) {
            LOGGER.error("Error during sign in", exception);
            throw new BadCredentialsException("Invalid username/password supplied!");
        }
    }

    @Override
    public TokenDTO refreshToken(String username, String refreshToken) {
        LOGGER.info("Request refresh token");

        User user = this.userRepository.findByUsername(username);

        if (user != null) {
            return this.jwtTokenProvider.createRefreshToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("Username: " + username + " not found");
        }
    }
}
