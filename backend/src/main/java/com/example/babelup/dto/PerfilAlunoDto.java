package com.example.babelup.dto;

import java.util.UUID;

public class PerfilAlunoDto {
    private String nome;
    private String email;
    private String nivelAtual;
    private int progressoGeral;
    private UUID nivelId;
    private String curso;

    public PerfilAlunoDto(String nome, String email, String nivelAtual, int progressoGeral) {
        this(nome, email, nivelAtual, progressoGeral, null, null);
    }

    public PerfilAlunoDto(String nome, String email, String nivelAtual, int progressoGeral, UUID nivelId, String curso) {
        this.nome = nome;
        this.email = email;
        this.nivelAtual = nivelAtual;
        this.progressoGeral = progressoGeral;
        this.nivelId = nivelId;
        this.curso = curso;
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

    public String getNivelAtual() {
        return nivelAtual;
    }

    public void setNivelAtual(String nivelAtual) {
        this.nivelAtual = nivelAtual;
    }

    public int getProgressoGeral() {
        return progressoGeral;
    }

    public void setProgressoGeral(int progressoGeral) {
        this.progressoGeral = progressoGeral;
    }

    public UUID getNivelId() {
        return nivelId;
    }

    public void setNivelId(UUID nivelId) {
        this.nivelId = nivelId;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
}
