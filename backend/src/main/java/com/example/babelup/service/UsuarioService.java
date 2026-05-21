package com.example.babelup.service;

import com.example.babelup.dto.UsuarioDto;
import com.example.babelup.entities.EnumPerfil;
import com.example.babelup.entities.Usuario;
import com.example.babelup.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;


    public Usuario cadastrarAluno(UsuarioDto novoAlunoDto) {
        if (usuarioRepository.existsByEmail(novoAlunoDto.getEmail())) {
            throw new IllegalArgumentException("Este e-mail já está em uso na BabelUp.");
        }
        Usuario novoAluno = new Usuario(novoAlunoDto);

        String senhaCriptografada = passwordEncoder.encode(novoAluno.getSenha());
        novoAluno.setSenha(senhaCriptografada);

        return usuarioRepository.save(novoAluno);
    }

    public Optional<Usuario> buscarUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email);
    }


    public List<UsuarioDto> findAll(){
        return usuarioRepository.findAll().stream()
                .map(UsuarioDto::new)
                .toList();
    }


    public void startDb(){
        List<UsuarioDto> usuariosIniciais = Arrays.asList(
                new UsuarioDto(null, "João Marcelo", "joao@gmail.com", "Joao123", EnumPerfil.ADMIN),
                new UsuarioDto(null, "Ludmila", "ludmila@gmail.com", "Ludmila123", EnumPerfil.PROFESSOR),
                new UsuarioDto(null, "Rodrigo Santos", "rodrigo@gmail.com", "Rodrigo123", EnumPerfil.ALUNO)
        );

        for (UsuarioDto u : usuariosIniciais) {

            if (!usuarioRepository.existsByEmail(u.getEmail())) {
                cadastrarAluno(u);
                System.out.println("Usuário criado na inicialização: " + u.getNome());
            }
        }
    }
}
