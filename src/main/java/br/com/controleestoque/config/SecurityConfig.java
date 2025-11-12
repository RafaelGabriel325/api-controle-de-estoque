package br.com.controleestoque.config;

import br.com.controleestoque.security.jwt.JwtConfigurer;
import br.com.controleestoque.security.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests
                                .requestMatchers("/**").permitAll()
                                .requestMatchers("/api/auth/sign-in","/api/auth/refresh-token/**","/swagger-ui/**","/v3/api-docs/**").permitAll()
                                .requestMatchers("/api/**","/swagger-ui/**","/v3/api-docs/**").hasAuthority("ADMIN")
                                .requestMatchers("/api/pessoa", "/api/pessoa/{id}",
                                        "/api/produto", "/api/produto/{id}",
                                        "/api/tipo-produto", "/api/tipo-produto/{id}",
                                        "/swagger-ui/**","/v3/api-docs/**").authenticated()
                                .requestMatchers("/api/auth/permission/delete","/api/auth/user/delete").denyAll()
                )
                .cors(Customizer.withDefaults())
                .apply(new JwtConfigurer(jwtTokenProvider));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
