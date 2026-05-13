package com.example.babelup.dto;

import com.example.babelup.entities.Perfil;
import com.example.babelup.entities.Usuario;

import java.lang.reflect.Field;

public class UsuarioDto {
    public Long id;
    public String nome;
    public String email;
    public Perfil perfil;
    public String senha;

    public UsuarioDto() {
    }

    public UsuarioDto(Usuario usuario){
        this.id = usuario.getId();
        this.email = usuario.getEmail();
        this.nome = usuario.getNome();
        this.perfil = usuario.getPerfil();
        this.senha = usuario.getSenha();
    }
}
