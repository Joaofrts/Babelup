package com.example.babelup.service;

import com.example.babelup.dto.ProfessorCadastroDto;
import com.example.babelup.entities.Enum.EnumPerfil;
import com.example.babelup.entities.usuarios.Usuario;
import com.example.babelup.repository.usuarios.UsuarioRepository;
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


    public Usuario cadastrarProfessor(ProfessorCadastroDto novoAlunoDto) {
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


    public List<ProfessorCadastroDto> findAll(){
        return usuarioRepository.findAll().stream()
                .map(ProfessorCadastroDto::new)
                .toList();
    }


    public void startDb(){
        List<ProfessorCadastroDto> usuariosIniciais = Arrays.asList(
                new ProfessorCadastroDto("João Marcelo", "joao@gmail.com", "Joao123", EnumPerfil.ADMIN),
                new ProfessorCadastroDto("Ludmila", "ludmila@gmail.com", "Ludmila123", EnumPerfil.PROFESSOR),
                new ProfessorCadastroDto("Rodrigo Santos", "rodrigo@gmail.com", "Rodrigo123", EnumPerfil.ALUNO)
        );

        for (ProfessorCadastroDto u : usuariosIniciais) {

            if (!usuarioRepository.existsByEmail(u.getEmail())) {
                cadastrarAluno(u);
                System.out.println("Usuário criado na inicialização: " + u.getNome());
            }
        }
    }
}
