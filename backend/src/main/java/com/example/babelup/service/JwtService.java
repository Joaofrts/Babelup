package com.example.babelup.service;

import com.example.babelup.entities.usuarios.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${api.security.token.secret}")
    private String CHAVE_SECRETA;

    @Value("${api.security.token.expiration_ms}")
    private long jwtExpirationMs;

    @Value("${api.security.token.refresh_expiration_ms}")
    private long refreshExpirationMs;

    private SecretKey getChaveAssinatura() {
        return Keys.hmacShaKeyFor(CHAVE_SECRETA.getBytes(StandardCharsets.UTF_8));
    }

    public String gerarToken(Usuario usuario) {
        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("perfil", usuario.getPerfil().name())
                .claim("nome", usuario.getNome())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getChaveAssinatura())
                .compact();
    }

    public String gerarRefreshToken(Usuario usuario) {
        return Jwts.builder()
                .subject(usuario.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshExpirationMs)) // 1 hora
                .signWith(getChaveAssinatura())
                .compact();
    }

    public boolean isTokenValido(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getChaveAssinatura())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            logger.error("Token JWT inválido ou expirado: {}", e.getMessage());
            return false;
        }
    }

    public String extrairEmailUsuario(String token) {
        return Jwts.parser().verifyWith(getChaveAssinatura())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}

