package com.example.babelup.configuration;

import com.example.babelup.service.AutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AutenticacaoService autenticacaoService;
    private final JwtAuthFilter jwtAuthFilter;


    public SecurityConfig(AutenticacaoService autenticacaoService, JwtAuthFilter jwtAuthFilter) {
        this.autenticacaoService = autenticacaoService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // 1. Ativando o CORS para o frontend React (porta 5173) poder se comunicar
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(authorize -> authorize

                        // Rotas públicas (Qualquer um pode acessar sem Token)
                        .requestMatchers(HttpMethod.POST, "/api/alunos/cadastro").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/autenticacao/login/aluno").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/autenticacao/login/adm").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/autenticacao/login/professor").permitAll()
                        .requestMatchers(HttpMethod.GET,"/swagger-ui/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/catalogo/cursos").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/autenticacao/refresh").permitAll()

                        // 2. Rotas restritas ao Administrador
                        .requestMatchers(HttpMethod.POST, "/api/modulos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/modulos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/modulos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/admin/**").hasRole("ADMIN")

                        // Qualquer outra requisição precisará de autenticação
                        .anyRequest().authenticated()
                ).logout(logout-> logout
                        .deleteCookies("remove")
                        .invalidateHttpSession(false)
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/api/admin/teste")
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // 3. Método que configura as regras do CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        // Permite os métodos necessários, incluindo o OPTIONS que o navegador faz sozinho
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    // Definir o algoritmo de criptografia de senhas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}