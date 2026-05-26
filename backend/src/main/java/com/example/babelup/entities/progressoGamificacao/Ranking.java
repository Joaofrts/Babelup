package com.example.babelup.entities.progressoGamificacao;

import com.example.babelup.entities.base.EntidadeAuditavel;
import com.example.babelup.entities.usuarios.Aluno;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.UUID;

@Entity
@Table(
        name = "ranking",
        uniqueConstraints = @UniqueConstraint(columnNames = {"aluno_id", "mes", "ano"}),
        indexes = {
                @Index(name = "idx_ranking_aluno", columnList = "aluno_id"),
                @Index(name = "idx_ranking_mes_ano", columnList = "mes,ano"),
                @Index(name = "idx_ranking_posicao", columnList = "posicao")
        })
public class Ranking extends EntidadeAuditavel {

    @NotNull(message = "Mês não pode ser nulo")
    @Min(value = 1, message = "Mês deve ser entre 1 e 12")
    @Max(value = 12, message = "Mês deve ser entre 1 e 12")
    @Column(nullable = false)
    private Integer mes;

    @NotNull(message = "Ano não pode ser nulo")
    @Positive(message = "Ano deve ser positivo")
    @Column(nullable = false)
    private Integer ano;

    @NotNull(message = "Posição não pode ser nula")
    @Positive(message = "Posição deve ser positiva")
    @Column(nullable = false)
    private Integer posicao;

    @NotNull(message = "Pontuação não pode ser nula")
    @PositiveOrZero(message = "Pontuação não pode ser negativa")
    @Column(nullable = false)
    private Integer pontuacao = 0;

    @NotNull(message = "Aluno não pode ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    public Ranking() {
        super();
    }

    public Ranking(Integer mes, Integer ano, Integer posicao, Integer pontuacao, Aluno aluno) {
        this();
        this.mes = mes;
        this.ano = ano;
        this.posicao = posicao;
        this.pontuacao = pontuacao;
        this.aluno = aluno;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getPosicao() {
        return posicao;
    }

    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }

    public Integer getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(Integer pontuacao) {
        this.pontuacao = pontuacao;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public String getPeriodo() {
        return String.format("%02d/%04d", mes, ano);
    }

    @Override
    public String toString() {
        return "Ranking{" +
                "id=" + id +
                ", aluno=" + (aluno != null ? aluno.getNome() : "null") +
                ", posicao=" + posicao +
                ", periodo=" + getPeriodo() +
                '}';
    }
}
