package com.example.babelup.service;

import com.example.babelup.entities.Usuario;
import com.example.babelup.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Vai ao banco procurar o e-mail
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        // 2. Se não encontrar, lança um erro que o Controller vai apanhar
        if (usuario.isEmpty()) {
            throw new UsernameNotFoundException("Utilizador não encontrado com o e-mail: " + email);
        }

        Usuario u = usuario.get();

        // 3. Converte o seu "Usuario" num "User" que o Spring Security consegue entender
        return User.builder()
                .username(u.getEmail())
                .password(u.getSenha())
                .roles(u.getPerfil().name())
                .build();
    }
}
