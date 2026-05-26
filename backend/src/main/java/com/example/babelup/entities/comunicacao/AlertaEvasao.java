package com.example.babelup.entities.comunicacao;

import com.example.babelup.entities.base.SoftDeleteEntity;
import com.example.babelup.entities.usuarios.Aluno;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "alerta_evasao",indexes = {
        @Index(name = "idx_alerta_aluno", columnList = "aluno_id"),
        @Index(name = "idx_alerta_visualizado", columnList = "visualizado"),
        @Index(name = "idx_alerta_criado_em", columnList = "criado_em")
})
public class AlertaEvasao extends SoftDeleteEntity {


    @Column(columnDefinition = "TEXT")
    @Size(max = 2000, message = "Os critérios de evasão não podem exceder 2000 caracteres.")
    private String criterios;

    @NotNull
    @Column(nullable = false,columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean visualizado = false;

    @NotNull(message = "Aluno não pode ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    public AlertaEvasao() {
        super();
    }

    public AlertaEvasao(String criterios, Aluno aluno) {
        this();
        this.criterios = criterios;
        this.aluno = aluno;
        this.visualizado = false;
    }

    public String getCriterios() {
        return criterios;
    }

    public void setCriterios(String criterios) {
        this.criterios = criterios;
    }

    public boolean isVisualizado() {
        return visualizado;
    }

    public void setVisualizado(boolean visualizado) {
        this.visualizado = visualizado;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    @Override
    public String toString() {
        return "AlertaEvasao{" +
                "id=" + id +
                ", aluno=" + (aluno != null ? aluno.getNome() : "null") +
                ", visualizado=" + visualizado +
                ", criadoEm=" + criadoEm +
                ", deletadoEm=" + deletadoEm +
                '}';
    }
}
