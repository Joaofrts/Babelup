package com.example.babelup.entities.progressoGamificacao;

import com.example.babelup.entities.Enum.EnumStatusProgresso;
import com.example.babelup.entities.base.EntidadeAuditavel;
import com.example.babelup.entities.estruturaAcademica.Modulo;
import com.example.babelup.entities.usuarios.Aluno;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "progresso_aluno",
        uniqueConstraints = @UniqueConstraint(columnNames = {"aluno_id","modulo_id"}),
        indexes = {
                @Index(name = "idx_progresso_aluno", columnList = "aluno_id"),
                @Index(name = "idx_progresso_modulo", columnList = "modulo_id"),
                @Index(name = "idx_progresso_status", columnList = "status")
        })
public class ProgressoAluno extends EntidadeAuditavel {

    @NotNull(message = "Percentual não pode ser nulo")
    @DecimalMin(value = "0.0", message = "Percentual não pode ser negativo")
    @DecimalMax(value = "100.0", message = "Percentual não pode ser maior que 100")
    @Column(name = "percentual_concluido", nullable = false)
    private Double percentualConcluido = 0.0;

    @NotNull(message = "Status não pode ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnumStatusProgresso status = EnumStatusProgresso.NAO_INICIADO;

    @NotNull(message = "Aluno não pode ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @NotNull(message = "Módulo não pode ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modulo_id", nullable = false)
    private Modulo modulo;

    public ProgressoAluno() {
        super();
    }

    public ProgressoAluno(Aluno aluno, Modulo modulo) {
        this();
        this.aluno = aluno;
        this.modulo = modulo;
        this.percentualConcluido = 0.0;
        this.status = EnumStatusProgresso.NAO_INICIADO;
    }

    public Double getPercentualConcluido() {
        return percentualConcluido;
    }

    public void setPercentualConcluido(Double percentualConcluido) {
        this.percentualConcluido = percentualConcluido;

        if (percentualConcluido >= 100.0) {
            this.status = EnumStatusProgresso.CONCLUIDO;
        } else if (percentualConcluido > 0.0) {
            this.status = EnumStatusProgresso.EM_ANDAMENTO;
        } else {
            this.status = EnumStatusProgresso.NAO_INICIADO;
        }
    }

    public EnumStatusProgresso getStatus() {
        return status;
    }

    public void setStatus(EnumStatusProgresso status) {
        this.status = status;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public LocalDateTime getUltimoAcesso() {
        return atualizadoEm;
    }

    public boolean isConcluido() {
        return status == EnumStatusProgresso.CONCLUIDO;
    }

    public boolean isIniciado() {
        return status != EnumStatusProgresso.NAO_INICIADO;
    }

    @Override
    public String toString() {
        return "ProgressoAluno{" +
                "id=" + id +
                ", aluno=" + (aluno != null ? aluno.getNome() : "null") +
                ", modulo=" + (modulo != null ? modulo.getTitulo() : "null") +
                ", percentual=" + percentualConcluido +
                ", status=" + status +
                '}';
    }
}
