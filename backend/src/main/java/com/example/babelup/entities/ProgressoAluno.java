package com.example.babelup.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "progresso_aluno", uniqueConstraints = @UniqueConstraint(columnNames = {"aluno_id","modulo_id"}))
public class ProgressoAluno {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "percentual_concluido", nullable = false)
    private Double percentualConcluido = 0.0;

    @Column(name = "ultimo_acesso")
    private LocalDateTime ultimoAcesso;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnumStatusProgresso status = EnumStatusProgresso.NAO_INICIADO;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "modulo_id", nullable = false)
    private Modulo modulo;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getPercentualConcluido() {
        return percentualConcluido;
    }

    public void setPercentualConcluido(Double percentualConcluido) {
        this.percentualConcluido = percentualConcluido;
    }

    public LocalDateTime getUltimoAcesso() {
        return ultimoAcesso;
    }

    public void setUltimoAcesso(LocalDateTime ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
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
}
