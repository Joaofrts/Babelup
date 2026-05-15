package com.example.babelup.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "agendamentos_conversacao")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Usuario professor;

    @ManyToOne
    @JoinColumn(name = "modulo_id", nullable = false)
    private Modulo modulo;

    @ManyToMany
    @JoinTable(
            name = "agendamento_alunos",
            joinColumns = @JoinColumn(name = "agendamento_id"),
            inverseJoinColumns = @JoinColumn(name = "aluno_id")
    )
    @JsonIgnore
    private List<Usuario> alunos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Usuario getProfessor() {
        return professor;
    }

    public void setProfessor(Usuario professor) {
        this.professor = professor;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public List<Usuario> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Usuario> alunos) {
        this.alunos = alunos;
    }
}
