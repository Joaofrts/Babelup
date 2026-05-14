package com.example.babelup.service;

import com.example.babelup.dto.UsuarioDto;
import com.example.babelup.entities.Perfil;
import com.example.babelup.entities.Usuario;
import com.example.babelup.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    private PasswordEncoder passwordEncoder; // Ferramenta do Spring Security para criptografia


    public Usuario cadastrarAluno(UsuarioDto novoAlunoDto) {
        if (usuarioRepository.existsByEmail(novoAlunoDto.getEmail())) {
            throw new IllegalArgumentException("Este e-mail já está em uso na BabelUp.");
        }
        Usuario novoAluno = new Usuario(novoAlunoDto);


        novoAluno.setPerfil(Perfil.ALUNO);

        String senhaCriptografada = passwordEncoder.encode(novoAluno.getSenha());
        novoAluno.setSenha(senhaCriptografada);

        return usuarioRepository.save(novoAluno);
    }


    public List<UsuarioDto> findAll(){
        return usuarioRepository.findAll().stream()
                .map(UsuarioDto::new)
                .toList();
    }


    public void startDb(){
        List<Usuario> usuariosIniciais = Arrays.asList(
                new Usuario(null, "João Marcelo", "joao@gmail.com", "Joao123", Perfil.ADMINISTRADOR),
                new Usuario(null, "Ludmila", "ludmila@gmail.com", "Ludmila123", Perfil.PROFESSOR),
                new Usuario(null, "Rodrigo Santos", "rodrigo@gmail.com", "Rodrigo123", Perfil.ALUNO)
        );

        for (Usuario u : usuariosIniciais) {

            if (!usuarioRepository.existsByEmail(u.getEmail())) {
                usuarioRepository.save(u);
                System.out.println("Usuário criado na inicialização: " + u.getNome());
            }
        }
    }
}
