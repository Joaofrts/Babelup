package com.example.babelup.controller;

import com.example.babelup.dto.LoginDto;
import com.example.babelup.entities.Usuario;
import com.example.babelup.service.JwtService;
import com.example.babelup.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/autenticacao")
public class AutenticacaoController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<Object> fazerLogin(@RequestBody LoginDto loginDto){
        try{
            UsernamePasswordAuthenticationToken credenciais =
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getSenha());


            Authentication autenticacao = authenticationManager.authenticate(credenciais);

            Optional<Usuario> usuarioLogado = usuarioService.buscarUsuarioPorEmail(loginDto.getEmail());

            String tokenGerado = jwtService.gerarToken(usuarioLogado.get());

            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", "Login realizado com sucesso");
            resposta.put("token", tokenGerado);
            resposta.put("perfil",usuarioLogado.get().getPerfil().name());

            return ResponseEntity.ok(resposta);
        }catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha incorretos.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Houve um erro inesperado.");
        }
    }
}
