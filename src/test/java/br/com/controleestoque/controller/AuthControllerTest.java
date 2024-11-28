package br.com.controleestoque.controller;

import br.com.controleestoque.security.AccountCredentialsDTO;
import br.com.controleestoque.security.TokenDTO;
import br.com.controleestoque.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final String MOCK_ACCESS_TOKEN = "mockAccessToken";
    private static final String MOCK_REFRESH_TOKEN = "mockRefreshToken";
    private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid credentials. Unable to sign in.";
    private static final String INVALID_CLIENT_REQUEST_MESSAGE = "Invalid client request! Check your parameters.";

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignInSuccess() {
        AccountCredentialsDTO credentials = new AccountCredentialsDTO(USERNAME, PASSWORD);
        TokenDTO token = TokenDTO.builder()
                .username(USERNAME)
                .authenticated(true)
                .created(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .accessToken(MOCK_ACCESS_TOKEN)
                .refreshToken(MOCK_REFRESH_TOKEN)
                .build();

        when(authService.signIn(any(AccountCredentialsDTO.class))).thenReturn(token);

        ResponseEntity<?> response = authController.signIn(credentials);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof TokenDTO);

        TokenDTO responseBody = (TokenDTO) response.getBody();
        assertEquals(USERNAME, responseBody.getUsername());
        assertTrue(responseBody.getAuthenticated());
        assertNotNull(responseBody.getCreated());
        assertNotNull(responseBody.getExpiration());
        assertEquals(MOCK_ACCESS_TOKEN, responseBody.getAccessToken());
        assertEquals(MOCK_REFRESH_TOKEN, responseBody.getRefreshToken());
    }

    @Test
    void testSignInFailureDueToInvalidCredentials() {
        AccountCredentialsDTO credentials = new AccountCredentialsDTO(USERNAME, PASSWORD);

        when(authService.signIn(any(AccountCredentialsDTO.class))).thenReturn(null);

        ResponseEntity<?> response = authController.signIn(credentials);
        assertEquals(401, response.getStatusCodeValue());
        assertEquals(INVALID_CREDENTIALS_MESSAGE, response.getBody());
    }

    @Test
    void testSignInFailureDueToBadRequest() {
        AccountCredentialsDTO credentials = new AccountCredentialsDTO("", "");

        ResponseEntity<?> response = authController.signIn(credentials);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(INVALID_CLIENT_REQUEST_MESSAGE, response.getBody());
    }

    @Test
    void testRefreshTokenSuccess() {
        String refreshToken = "validRefreshToken";
        TokenDTO token = TokenDTO.builder()
                .username(USERNAME)
                .authenticated(true)
                .created(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .accessToken("newAccessToken")
                .refreshToken("newRefreshToken")
                .build();

        when(authService.refreshToken(eq(USERNAME), eq(refreshToken))).thenReturn(token);

        ResponseEntity<?> response = authController.refreshToken(USERNAME, refreshToken);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertInstanceOf(TokenDTO.class, response.getBody());

        TokenDTO responseBody = (TokenDTO) response.getBody();
        assertEquals(USERNAME, responseBody.getUsername());
        assertEquals("newAccessToken", responseBody.getAccessToken());
        assertEquals("newRefreshToken", responseBody.getRefreshToken());
    }

    @Test
    void testRefreshTokenFailureDueToInvalidToken() {
        String refreshToken = "invalidRefreshToken";

        when(authService.refreshToken(eq(USERNAME), eq(refreshToken))).thenReturn(null);

        ResponseEntity<?> response = authController.refreshToken(USERNAME, refreshToken);
        assertEquals(401, response.getStatusCodeValue());
        assertEquals(INVALID_CREDENTIALS_MESSAGE, response.getBody());
    }

    @Test
    void testRefreshTokenFailureDueToBadRequest() {
        ResponseEntity<?> response = authController.refreshToken("", "");
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(INVALID_CLIENT_REQUEST_MESSAGE, response.getBody());
    }
}