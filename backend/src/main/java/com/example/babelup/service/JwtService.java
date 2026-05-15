package com.example.babelup.service;

import com.example.babelup.entities.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;
@Service
public class JwtService {
    @Value("${api.security.token.secret}")
    private String SECRET_KEY ;

    private final long EXPIRATION_TIME_MILISECONDS = 7200000;

    public String gerarToken(Usuario usuario){
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("perfil", usuario.getPerfil().name())
                .claim("nome",usuario.getNome())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME_MILISECONDS))
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .compact();
    }
}
