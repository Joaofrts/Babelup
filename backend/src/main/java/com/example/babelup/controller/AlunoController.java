package com.example.babelup.controller;

import com.example.babelup.dto.PerfilAlunoDto;
import com.example.babelup.entities.usuarios.Usuario;
import com.example.babelup.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/meu-perfil")
    public ResponseEntity<PerfilAlunoDto> obterMeuPerfil(@AuthenticationPrincipal UserDetails userDetails) {
        // 1. O Spring Security injeta o usuário logado aqui graças ao nosso JwtAuthFilter
        if (userDetails == null) {
            return ResponseEntity.status(401).build(); // Não autorizado
        }

        // 2. Extraímos o e-mail do token já validado
        String email = userDetails.getUsername();

        // 3. Buscamos as informações completas no banco de dados
        Optional<Usuario> usuarioOpt = usuarioService.buscarUsuarioPorEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // NOTA: Para atender ao Requisito RF-011, futuramente buscaremos este progresso
            // através do ProgressoService. Por agora, passaremos dados estáticos
            // apenas para ver a barra de progresso no React funcionar!
            String nivelAtual = "Iniciante";
            int progressoGeral = 25;

            // 4. Montamos o DTO para enviar ao React
            PerfilAlunoDto perfil = new PerfilAlunoDto(
                    usuario.getNome(),
                    usuario.getEmail(),
                    nivelAtual,
                    progressoGeral
            );

            return ResponseEntity.ok(perfil);
        }

        return ResponseEntity.notFound().build();
    }
}
