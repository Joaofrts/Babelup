package com.example.babelup.controller;

import com.example.babelup.dto.NovoUsuarioDto;
import com.example.babelup.dto.PerfilAlunoDto;
import com.example.babelup.dto.RespostaModuloDto;
import com.example.babelup.entities.estruturaAcademica.Modulo;
import com.example.babelup.entities.usuarios.Usuario;
import com.example.babelup.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/meu-perfil")
    public ResponseEntity<PerfilAlunoDto> obterMeuPerfil(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build(); // Não autorizado
        }

        String email = userDetails.getUsername();

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
