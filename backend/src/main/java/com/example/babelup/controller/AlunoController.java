package com.example.babelup.controller;

import com.example.babelup.dto.NovoUsuarioDto;
import com.example.babelup.dto.PerfilAlunoDto;
import com.example.babelup.entities.estruturaAcademica.Nivel;
import com.example.babelup.entities.usuarios.Aluno;
import com.example.babelup.entities.usuarios.Usuario;
import com.example.babelup.service.ProgressoService;
import com.example.babelup.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProgressoService progressoService;

    @GetMapping("/meu-perfil")
    public ResponseEntity<PerfilAlunoDto> obterMeuPerfil(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }

        String email = userDetails.getUsername();
        Optional<Usuario> usuarioOpt = usuarioService.buscarUsuarioPorEmail(email);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = usuarioOpt.get();

        if (!(usuario instanceof Aluno aluno)) {
            return ResponseEntity.status(403).build();
        }

        Nivel nivel = aluno.getNivelAtual();
        String nivelAtual = nivel != null ? nivel.getNome() : "Nao definido";
        String curso = nivel != null ? nivel.getIdioma() + " - " + nivel.getNome() : null;
        UUID nivelId = nivel != null ? nivel.getId() : null;
        int progressoGeral = progressoService.atualizarProgressoGeralDoNivel(aluno);

        PerfilAlunoDto perfil = new PerfilAlunoDto(
                usuario.getNome(),
                usuario.getEmail(),
                nivelAtual,
                progressoGeral,
                nivelId,
                curso
        );

        return ResponseEntity.ok(perfil);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrarAluno(@RequestBody NovoUsuarioDto dto) {
        try {
            usuarioService.cadastrarUsuario(dto);
            return ResponseEntity.ok("Aluno cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Houve um erro inesperado ao cadastrar o aluno.");
        }
    }
}
