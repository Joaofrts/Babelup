package com.example.babelup.dto;

import java.util.UUID;

public class UsuarioRespostaDTO {
    private UUID id;
    private String nome;
    private String email;
    private String perfil;
    private String curso;

    public UsuarioRespostaDTO(UUID id, String nome, String email, String perfil) {
        this(id, nome, email, perfil, null);
    }

    public UsuarioRespostaDTO(UUID id, String nome, String email, String perfil, String curso) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
        this.curso = curso;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
}
