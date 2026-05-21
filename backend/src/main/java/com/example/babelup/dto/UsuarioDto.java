package com.example.babelup.dto;

import com.example.babelup.entities.EnumPerfil;
import com.example.babelup.entities.Usuario;

import java.util.UUID;

public class UsuarioDto {

    private UUID id;
    private String nome;
    private String email;
    private EnumPerfil enumPerfil;
    private String senha;

    public UsuarioDto() {
    }

    public UsuarioDto(Usuario usuario){
        this.id = usuario.getId();
        this.email = usuario.getEmail();
        this.nome = usuario.getNome();
        this.enumPerfil = usuario.getPerfil();
        this.senha = usuario.getSenha();
    }

    public UsuarioDto(UUID id, String nome, String email, String senha, EnumPerfil enumPerfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.enumPerfil = enumPerfil;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public EnumPerfil getPerfil() {
        return enumPerfil;
    }

    public String getSenha() {
        return senha;
    }
}
