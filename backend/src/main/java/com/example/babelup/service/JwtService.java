package com.example.babelup.service;

import com.example.babelup.entities.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
@Service
public class JwtService {
    @Value("${api.security.token.secret}")
    private String CHAVE_SECRETA;

    private final long EXPIRACAO_MILISSEGUNDOS = 7200000; // 2 horas

    // Única fonte da verdade para a chave de criptografia
    private Key getChaveAssinatura() {
        return Keys.hmacShaKeyFor(CHAVE_SECRETA.getBytes(StandardCharsets.UTF_8));
    }

    public String gerarToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("perfil", usuario.getPerfil().name())
                .claim("nome", usuario.getNome())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRACAO_MILISSEGUNDOS))
                // ASSINANDO com a chave nova
                .signWith(getChaveAssinatura(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValido(String token) {
        try {
            // VERIFICANDO com a chave nova
            Jwts.parserBuilder().setSigningKey(getChaveAssinatura()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao validar token: " + e.getMessage());
            return false;
        }
    }

    public String extrairEmailUsuario(String token) {
        // EXTRAINDO com a chave nova
        return Jwts.parserBuilder()
                .setSigningKey(getChaveAssinatura())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}

