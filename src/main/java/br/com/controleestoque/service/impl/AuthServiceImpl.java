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

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenDTO signIn(AccountCredentialsDTO accountCredentialsDTO) {
        LOGGER.info("Signing in user: {}", accountCredentialsDTO.getUsername());

        User user = userRepository.findByUsername(accountCredentialsDTO.getUsername());
        if (user == null) {
            logAndThrowUsernameNotFound(accountCredentialsDTO.getUsername());
        }

        if (user != null && !passwordEncoder.matches(accountCredentialsDTO.getPassword(), user.getPassword())) {
            LOGGER.error("Invalid password for username: {}", accountCredentialsDTO.getUsername());
            throw new BadCredentialsException("Invalid username/password supplied!");
        }

        assert user != null;
        return jwtTokenProvider.createAccessToken(user.getUsername(), user.getPermissions());
    }

    @Override
    public TokenDTO refreshToken(String username, String refreshToken) {
        LOGGER.info("Refreshing token for user: {}", username);

        User user = userRepository.findByUsername(username);
        if (user == null) {
            logAndThrowUsernameNotFound(username);
        }

        return jwtTokenProvider.createRefreshToken(refreshToken);
    }

    private void logAndThrowUsernameNotFound(String username) {
        LOGGER.error("Username: {} not found", username);
        throw new UsernameNotFoundException("Username: " + username + " not found");
    }
}