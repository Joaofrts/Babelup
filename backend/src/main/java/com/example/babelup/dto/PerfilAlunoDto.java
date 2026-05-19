package com.example.babelup.dto;

public class PerfilAlunoDto {
    private String nome;
    private String email;
    private String nivelAtual;
    private int progressoGeral;

    public PerfilAlunoDto(String nome, String email, String nivelAtual, int progressoGeral) {
        this.nome = nome;
        this.email = email;
        this.nivelAtual = nivelAtual;
        this.progressoGeral = progressoGeral;
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
}
