package com.example.babelup.controller;

import com.example.babelup.dto.LoginDto;
import com.example.babelup.dto.RefreshTokenRequisicaoDto;
import com.example.babelup.entities.usuarios.Usuario;
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
    public ResponseEntity<Object> fazerLogin(@RequestBody LoginDto loginDto) {
        try {
            UsernamePasswordAuthenticationToken credenciais =
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getSenha());

            // Tenta autenticar o usuário
            Authentication autenticacao = authenticationManager.authenticate(credenciais);

            // Busca o usuário no banco de dados
            Optional<Usuario> usuarioLogado = usuarioService.buscarUsuarioPorEmail(loginDto.getEmail());
            Usuario usuario = usuarioLogado.get();


            String accessToken = jwtService.gerarToken(usuario);
            String refreshToken = jwtService.gerarRefreshToken(usuario);

            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", "Login realizado com sucesso");
            resposta.put("token", accessToken);
            resposta.put("refreshToken", refreshToken);
            resposta.put("perfil", usuario.getPerfil().name());

            return ResponseEntity.ok(resposta);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha incorretos.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Houve um erro inesperado.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(){
        return ResponseEntity.accepted().body("Deslogado");
    }


    @PostMapping("/refresh")
    public ResponseEntity<Object> refresh(@RequestBody RefreshTokenRequisicaoDto request) {
        String requestRefreshToken = request.getRefreshToken();

        try {
            String email = jwtService.extrairEmailUsuario(requestRefreshToken);

            if (email != null) {

                Optional<Usuario> usuarioOpt = usuarioService.buscarUsuarioPorEmail(email);

                if (usuarioOpt.isPresent() && jwtService.isTokenValido(requestRefreshToken)) {

                    Usuario usuario = usuarioOpt.get();

                    String novoAccessToken = jwtService.gerarToken(usuario);
                    String novoRefreshToken = jwtService.gerarRefreshToken(usuario);

                    Map<String, String> resposta = new HashMap<>();
                    resposta.put("token", novoAccessToken);
                    resposta.put("refreshToken", novoRefreshToken);
                    resposta.put("perfil", usuario.getPerfil().name());

                    return ResponseEntity.ok(resposta);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao validar refresh token: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Sessão encerrada. Faça login novamente.");
    }
}