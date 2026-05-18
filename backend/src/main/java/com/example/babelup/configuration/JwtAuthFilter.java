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

    // NOVO MÉTODO AQUI: Define quais rotas NÃO devem passar por este filtro
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        // Ignora o filtro se for a rota de login ou cadastro
        return path.startsWith("/api/autenticacao/login") || path.startsWith("/api/usuarios/cadastro");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Se for uma requisição OPTIONS (Preflight do CORS), libera imediatamente sem checar token
        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            System.out.println("1. Token recebido no Filtro!");

            try {
                email = jwtService.extrairEmailUsuario(token);
                System.out.println("2. E-mail extraído: " + email);
            } catch (Exception e) {
                System.out.println("ERRO AO EXTRAIR E-MAIL: " + e.getMessage());
            }
        } else {
            // Agora, se cair aqui, é porque uma rota protegida (como Dashboard)
            // foi acessada sem o token, aí sim é um aviso válido!
            System.out.println("AVISO: Requisição em rota protegida sem token JWT.");
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = autenticacaoService.loadUserByUsername(email);

            if (jwtService.isTokenValido(token)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}