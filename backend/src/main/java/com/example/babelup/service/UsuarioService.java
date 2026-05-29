package com.example.babelup.service;

import com.example.babelup.dto.NovoUsuarioDto;
import com.example.babelup.dto.UsuarioRespostaDTO;
import com.example.babelup.entities.enumEntities.EnumPerfil;
import com.example.babelup.entities.usuarios.Usuario;
import com.example.babelup.factory.UsuarioFactory;
import com.example.babelup.repository.usuarios.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {


    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;
    private final UsuarioFactory usuarioFactory;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, UsuarioFactory usuarioFactory) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioFactory = usuarioFactory;
    }


    public Usuario cadastrarUsuario(NovoUsuarioDto usuarioDto) {
        if (usuarioRepository.existsByEmail(usuarioDto.email())) {
            throw new IllegalArgumentException("Este e-mail já está em uso na BabelUp.");
        }
        Usuario usuario = usuarioFactory.fabricar(usuarioDto);

        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email);
    }


    public List<UsuarioRespostaDTO> findAll(){
        return usuarioRepository.findAll().stream()
                .map(u-> new UsuarioRespostaDTO(u.getNome(), u.getEmail(), u.getPerfil().name()))
                .toList();
    }


    public void startDb(){
        List<NovoUsuarioDto> usuariosIniciais = Arrays.asList(
                new NovoUsuarioDto("João Marcelo", "joao@gmail.com", "Joao123456", EnumPerfil.ADMIN, null, true,false,null),
                new NovoUsuarioDto("Ludmila", "ludmila@gmail.com", "Ludmila123", EnumPerfil.PROFESSOR, null, true,false,null),
                new NovoUsuarioDto("Rodrigo Santos", "rodrigo@gmail.com", "Rodrigo123", EnumPerfil.ALUNO, null, true,false,null)
        );

        for (NovoUsuarioDto u : usuariosIniciais) {

            if (!usuarioRepository.existsByEmail(u.email())) {
                cadastrarUsuario(u);
                System.out.println("Usuário criado na inicialização: " + u.nome());
            }
        }
    }
}
