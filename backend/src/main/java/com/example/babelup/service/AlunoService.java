package com.example.babelup.service;

import com.example.babelup.dto.PerfilAlunoDto;
import com.example.babelup.entities.Usuario;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AlunoService {


    public static @NonNull PerfilAlunoDto getPerfilAlunoDto(Optional<Usuario> usuarioOpt) {
        Usuario usuario = usuarioOpt.get();

        // NOTA: Para atender ao Requisito RF-011, futuramente buscaremos este progresso
        // através do ProgressoService. Por agora, passaremos dados estáticos
        // apenas para ver a barra de progresso no React funcionar!
        String nivelAtual = "Iniciante";
        int progressoGeral = 25;

        // 4. Montamos o DTO para enviar ao React
        return new PerfilAlunoDto(
                usuario.getNome(),
                usuario.getEmail(),
                nivelAtual,
                progressoGeral
        );
    }
}
