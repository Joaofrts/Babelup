package com.example.babelup.service;

import com.example.babelup.dto.UsuarioDto;
import com.example.babelup.entities.Perfil;
import com.example.babelup.entities.Usuario;
import com.example.babelup.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    public void addUsuario(UsuarioDto dto){
        Usuario usuario = new Usuario(dto);
        repository.save(usuario);
    }

    public List<Usuario> findAll(){
        return repository.findAll();
    }

    public void startDb(){
        Usuario u1 = new Usuario(null,"João Marcelo","joao@gmail.com","Joao123", Perfil.ADMINISTRADOR);
        Usuario u2 = new Usuario(null, "Ludmila", "ludmila@gmmail.com", "Ludmila123", Perfil.PROFESSOR);
        Usuario u3 = new Usuario(null, "Rodrigo Santos", "rodrigo@gmail.com", "Rodrigo123", Perfil.ALUNO);
    }
}
