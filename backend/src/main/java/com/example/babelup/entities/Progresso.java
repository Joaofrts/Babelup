package com.example.babelup.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "progresso_alunos")
public class Progresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Usuario aluno;

    @ManyToOne
    @JoinColumn(name = "modulo_id", nullable = false)
    private Modulo modulo;

    @Column(name = "exercicio_concluido")
    private Boolean exercicioConcluido = false;

    @Column(name = "nota_exercicio")
    private Double notaExercicio;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getAluno() {
        return aluno;
    }

    public void setAluno(Usuario aluno) {
        this.aluno = aluno;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public Boolean getExercicioConcluido() {
        return exercicioConcluido;
    }

    public void setExercicioConcluido(Boolean exercicioConcluido) {
        this.exercicioConcluido = exercicioConcluido;
    }

    public Double getNotaExercicio() {
        return notaExercicio;
    }

    public void setNotaExercicio(Double notaExercicio) {
        this.notaExercicio = notaExercicio;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }
}
