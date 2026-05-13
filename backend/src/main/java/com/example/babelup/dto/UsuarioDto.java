package com.example.babelup.dto;

import com.example.babelup.entities.Perfil;
import com.example.babelup.entities.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.lang.reflect.Field;

public class UsuarioDto {
    private Long id;
    private String nome;
    private String email;
    private Perfil perfil;
    @JsonIgnore
    private String senha;

    public UsuarioDto() {
    }

    public UsuarioDto(Usuario usuario){
        this.id = usuario.getId();
        this.email = usuario.getEmail();
        this.nome = usuario.getNome();
        this.perfil = usuario.getPerfil();
        this.senha = usuario.getSenha();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public String getSenha() {
        return senha;
    }
}
