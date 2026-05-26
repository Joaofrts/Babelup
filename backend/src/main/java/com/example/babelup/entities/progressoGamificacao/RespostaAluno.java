package com.example.babelup.entities.progressoGamificacao;

import com.example.babelup.entities.base.EntidadeAuditavel;
import com.example.babelup.entities.pratica.Exercicio;
import com.example.babelup.entities.usuarios.Aluno;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "resposta_aluno",indexes = {
        @Index(name = "idx_resposta_aluno", columnList = "aluno_id"),
        @Index(name = "idx_resposta_exercicio", columnList = "exercicio_id"),
        @Index(name = "idx_resposta_correto", columnList = "correto")
})
public class RespostaAluno extends EntidadeAuditavel {

    @Column(columnDefinition = "TEXT")
    @Size(max = 5000, message = "Respostanão pode exceder 5000 caracteres")
    private String resposta;

    @NotNull(message = "Correto não pode ser nulo")
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean correto;

    @NotNull(message = "Tentativa não pode ser nula")
    @Positive(message = "Tentativa deve ser um número positivo")
    @Column(nullable = false)
    private Integer tentativa = 1;

    @NotNull(message = "Aluno não pode ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @NotNull(message = "Exercício não pode ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercicio_id",nullable = false)
    private Exercicio exercicio;

    public RespostaAluno() {
        super();
    }

    public RespostaAluno(String resposta, boolean correto, Aluno aluno, Exercicio exercicio) {
        this();
        this.resposta = resposta;
        this.correto = correto;
        this.aluno = aluno;
        this.exercicio = exercicio;
        this.tentativa = 1;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public boolean isCorreto() {
        return correto;
    }

    public void setCorreto(Boolean correto) {
        this.correto = correto;
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

    @Override
    public String toString() {
        return "RespostaAluno{" +
                "id=" + id +
                ", aluno=" + (aluno != null ? aluno.getNome() : "null") +
                ", correto=" + correto +
                ", tentativa=" + tentativa +
                '}';
    }
}
