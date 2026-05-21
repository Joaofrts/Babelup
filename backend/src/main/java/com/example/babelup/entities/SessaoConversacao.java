package com.example.babelup.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "sessao_conversacao")
public class SessaoConversacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "modalidade",nullable = false, length = 20)
    private EnumModalidadeSessao modalidadeSessao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private EnumStatusSessao status = EnumStatusSessao.AGENDADA;

    @Column(name = "gravacao_url",columnDefinition = "TEXT")
    private String gravacaoUrl;

    @Column(name = "max_alunos", nullable = false)
    private Integer maxAlunos = 5;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "modulo_id")
    private Modulo modulo;

    @ManyToMany
    @JoinTable( name = "sessao_aluno",
                joinColumns = @JoinColumn(name = "sessao_id"),
                inverseJoinColumns = @JoinColumn(name = "aluno_id"))
    private List<Aluno> alunos = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public EnumModalidadeSessao getModalidadeSessao() {
        return modalidadeSessao;
    }

    public void setModalidadeSessao(EnumModalidadeSessao modalidadeSessao) {
        this.modalidadeSessao = modalidadeSessao;
    }

    public EnumStatusSessao getStatus() {
        return status;
    }

    public void setStatus(EnumStatusSessao status) {
        this.status = status;
    }

    public String getGravacaoUrl() {
        return gravacaoUrl;
    }

    public void setGravacaoUrl(String gravacaoUrl) {
        this.gravacaoUrl = gravacaoUrl;
    }

    public Integer getMaxAlunos() {
        return maxAlunos;
    }

    public void setMaxAlunos(Integer maxAlunos) {
        this.maxAlunos = maxAlunos;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }
}
