package com.example.babelup.configuration;

import com.example.babelup.service.AutenticacaoService;
import com.example.babelup.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Pega o cabeçalho de Autorização da requisição
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        // 2. Verifica se o cabeçalho tem o token JWT (ele sempre começa com "Bearer ")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            System.out.println("1. Token recebido: " + token); // <--- LOG AQUI

            try {
                email = jwtService.extrairEmailUsuario(token);
                System.out.println("2. E-mail extraído do token: " + email); // <--- LOG AQUI
            } catch (Exception e) {
                System.out.println("ERRO AO EXTRAIR E-MAIL: " + e.getMessage());
            }
        } else {
            System.out.println("AVISO: Requisição chegou sem token ou sem a palavra Bearer");
        }

        // 3. Se achou um e-mail e o usuário ainda não está autenticado no contexto atual
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = autenticacaoService.loadUserByUsername(email);

            // 4. Se o token for válido, avisa ao Spring Security que este usuário está autenticado
            if (jwtService.isTokenValido(token)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Salva a autenticação na memória apenas durante essa requisição
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 5. Libera a requisição para continuar o seu caminho (para o Controller)
        filterChain.doFilter(request, response);
    }
}
