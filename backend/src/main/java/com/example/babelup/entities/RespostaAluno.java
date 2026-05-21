package com.example.babelup.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "resposta_aluno")
public class RespostaAluno {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String resposta;

    @Column(nullable = false)
    private Boolean correto;

    @Column(name = "data_resposta", nullable = false, updatable = false)
    private LocalDateTime dataResposta;

    @Column(nullable = false)
    private Integer tentativa = 1;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "exercicio_id",nullable = false)
    private Exercicio exercicio;

    @PrePersist
    protected void onCreate(){
        this.dataResposta = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public Boolean getCorreto() {
        return correto;
    }

    public void setCorreto(Boolean correto) {
        this.correto = correto;
    }

    public LocalDateTime getDataResposta() {
        return dataResposta;
    }

    public void setDataResposta(LocalDateTime dataResposta) {
        this.dataResposta = dataResposta;
    }

    public Integer getTentativa() {
        return tentativa;
    }

    public void setTentativa(Integer tentativa) {
        this.tentativa = tentativa;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Exercicio getExercicio() {
        return exercicio;
    }

    public void setExercicio(Exercicio exercicio) {
        this.exercicio = exercicio;
    }
}
