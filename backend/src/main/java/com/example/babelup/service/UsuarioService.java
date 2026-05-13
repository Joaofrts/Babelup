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

    @Autowired
    private PasswordEncoder passwordEncoder; // Ferramenta do Spring Security para criptografia

    public Usuario cadastrarAluno(Usuario novoAluno) {
        // 1. Regra de Negócio: Verificar se o e-mail já existe
        if (usuarioRepository.existsByEmail(novoAluno.getEmail())) {
            throw new IllegalArgumentException("Este e-mail já está em uso na BabelUp.");
        }

        // 2. Regra de Negócio: Forçar o perfil como ALUNO (por segurança)
        novoAluno.setPerfil(Perfil.ALUNO);

        // 3. Regra de Segurança: Criptografar a senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(novoAluno.getSenha());
        novoAluno.setSenha(senhaCriptografada);

        // 4. Salvar no banco usando o Repositório
        return usuarioRepository.save(novoAluno);
    }

    // 2. Uso de Streams para conversão limpa
    public List<UsuarioDto> findAll(){
        return repository.findAll().stream()
                .map(UsuarioDto::new)
                .toList();
    }

    // 3. startDb sem usar DTOs e protegido contra duplicidade
    public void startDb(){
        List<Usuario> usuariosIniciais = Arrays.asList(
                new Usuario(null, "João Marcelo", "joao@gmail.com", "Joao123", Perfil.ADMINISTRADOR),
                new Usuario(null, "Ludmila", "ludmila@gmail.com", "Ludmila123", Perfil.PROFESSOR),
                new Usuario(null, "Rodrigo Santos", "rodrigo@gmail.com", "Rodrigo123", Perfil.ALUNO)
        );

        for (Usuario u : usuariosIniciais) {
            // Requer que o método existsByEmail exista lá na sua interface UsuarioRepository
            if (!repository.existsByEmail(u.getEmail())) {
                repository.save(u);
                System.out.println("Usuário criado na inicialização: " + u.getNome());
            }
        }
    }
}
