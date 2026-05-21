package com.example.babelup.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="aluno")
public class Aluno extends Usuario{
    @Column(name= "nivel_atual",length = 50)
    private String nivelAtual;

    @Column(name= "progresso_geral", nullable = false)
    private Double progressoGeral = 0.0;

    @Column(name="pontuacao_ranking",nullable = false)
    private Integer pontuacaoRanking = 0;

    public String getNivelAtual() {
        return nivelAtual;
    }

    public void setNivelAtual(String nivelAtual) {
        this.nivelAtual = nivelAtual;
    }

    public Double getProgressoGeral() {
        return progressoGeral;
    }

    public void setProgressoGeral(Double progressoGeral) {
        this.progressoGeral = progressoGeral;
    }

    public Integer getPontuacaoRanking() {
        return pontuacaoRanking;
    }

    public void setPontuacaoRanking(Integer pontuacaoRanking) {
        this.pontuacaoRanking = pontuacaoRanking;
    }
}
