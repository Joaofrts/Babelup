package com.example.babelup.controller;

import com.example.babelup.dto.PerfilAlunoDto;
import com.example.babelup.entities.Usuario;
import com.example.babelup.service.AlunoService;
import com.example.babelup.service.UsuarioService;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.example.babelup.service.AlunoService.getPerfilAlunoDto;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private AlunoService alunoService;

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
            PerfilAlunoDto perfil = getPerfilAlunoDto(usuarioOpt);

            return ResponseEntity.ok(perfil);
        }

        return ResponseEntity.notFound().build();
    }


}
