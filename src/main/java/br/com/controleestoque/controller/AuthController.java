package br.com.controleestoque.controller;

import br.com.controleestoque.security.AccountCredentialsDTO;
import br.com.controleestoque.security.TokenDTO;
import br.com.controleestoque.service.AuthService;
import br.com.controleestoque.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static br.com.controleestoque.shared.constant.PathsConstants.*;

@RestController
@RequestMapping(AUTH_BASE)
@Tag(name = "Autenticação", description = "Endpoints para Gerenciar às Autenticação")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    private static final String INVALID_CLIENT_REQUEST = "Invalid client request! Check your parameters.";
    private static final String INVALID_CREDENTIALS = "Invalid credentials. Unable to sign in.";

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = AUTH_SIGN_IN,
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML},
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Autenticação do Usuário",
            description = "Autentica um usuário com as credenciais fornecidas",
            tags = {"Autenticação"},
            responses = {
                    @ApiResponse(description = "Sucesso", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TokenDTO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            })
    public ResponseEntity<?> signIn(@RequestBody AccountCredentialsDTO accountCredentialsDTO) {
        LOGGER.info("Request to sign in");

        if (areSignInParamsInvalid(accountCredentialsDTO)) {
            return ResponseEntity.badRequest().body(INVALID_CLIENT_REQUEST);
        }

        TokenDTO token = authService.signIn(accountCredentialsDTO);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(INVALID_CREDENTIALS);
        }

        return ResponseEntity.ok(token);
    }

    @PutMapping(value = AUTH_REFRESH_TOKEN,
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Atualização de Token de Acesso",
            description = "Atualiza o token de acesso usando um token de atualização válido",
            tags = {"Autenticação"},
            responses = {
                    @ApiResponse(description = "Sucesso", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TokenDTO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            })
    public ResponseEntity<?> refreshToken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken) {
        LOGGER.debug("Request refresh token");

        if (areRefreshTokenParamsInvalid(username, refreshToken)) {
            return ResponseEntity.badRequest().body(INVALID_CLIENT_REQUEST);
        }

        TokenDTO token = authService.refreshToken(username, refreshToken);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(INVALID_CREDENTIALS);
        }

        return ResponseEntity.ok(token);
    }

    private boolean areSignInParamsInvalid(AccountCredentialsDTO accountCredentialsDTO) {
        return accountCredentialsDTO == null ||
                accountCredentialsDTO.getUsername() == null || accountCredentialsDTO.getUsername().isBlank() ||
                accountCredentialsDTO.getPassword() == null || accountCredentialsDTO.getPassword().isBlank();
    }

    private boolean areRefreshTokenParamsInvalid(String username, String refreshToken) {
        return refreshToken == null || refreshToken.isBlank() || username == null || username.isBlank();
    }
}