package com.example.babelup.dto;

import com.example.babelup.entities.Enum.EnumPerfil;

public class ProfessorCadastroDto {

    private String nome;
    private String email;
    private EnumPerfil enumPerfil;
    private String senha;

    public ProfessorCadastroDto() {
    }

    public ProfessorCadastroDto(String nome, String email, String senha, EnumPerfil enumPerfil) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.enumPerfil = enumPerfil;
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
