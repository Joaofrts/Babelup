package com.example.babelup.entities.avaliacao;

import com.example.babelup.entities.enumEntities.EnumModalidadeSessao;
import com.example.babelup.entities.enumEntities.EnumStatusSessao;
import com.example.babelup.entities.enumEntities.EnumTipoSessao;
import com.example.babelup.entities.base.EntidadeAuditavel;
import com.example.babelup.entities.estruturaAcademica.Modulo;
import com.example.babelup.entities.usuarios.Aluno;
import com.example.babelup.entities.usuarios.Professor;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "sessao_conversacao", indexes = {
        @Index(name = "idx_sessao_professor", columnList = "professor_id"),
        @Index(name = "idx_sessao_modulo", columnList = "modulo_id"),
        @Index(name = "idx_sessao_status", columnList = "status")
})
public class SessaoConversacao extends EntidadeAuditavel {

    @NotNull(message = "Tipo de sessão não pode ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_sessao", nullable = false, length = 30)
    private EnumTipoSessao tipoSessao;

    @NotNull(message = "Data e hora da sessão não pode ser nula")
    @FutureOrPresent(message = "Data e hora da sessão deve ser no presente ou futuro")
    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @NotNull(message = "Modalidade da sessão não pode ser nula")
    @Enumerated(EnumType.STRING)
    @Column(name = "modalidade",nullable = false, length = 20)
    private EnumModalidadeSessao modalidadeSessao;

    @NotNull(message = "Status da sessão não pode ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'AGENDADA'")
    private EnumStatusSessao status = EnumStatusSessao.AGENDADA;

    @Column(name = "gravacao_url",columnDefinition = "TEXT")
    @Size(max = 2000, message = "URL de gravação deve ter no máximo 2000 caracteres")
    private String gravacaoUrl;

    @NotNull(message = "Número máximo de alunos não pode ser nulo")
    @Positive(message = "Número máximo de alunos deve ser positivo")
    @Max(value = 100, message = "Número máximo de alunos deve ser no máximo 100")
    @Column(name = "max_alunos", nullable = false)
    private Integer maxAlunos = 5;

    @NotNull(message = "Professor responsável pela sessão não pode ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modulo_id")
    private Modulo modulo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable( name = "sessao_aluno",
                joinColumns = @JoinColumn(name = "sessao_id"),
                inverseJoinColumns = @JoinColumn(name = "aluno_id"),
                indexes = {
                    @Index(name = "idx_sessao_aluno_sessao", columnList = "sessao_id"),
                    @Index(name = "idx_sessao_aluno_aluno", columnList = "aluno_id")
                })
    private List<Aluno> alunos = new ArrayList<>();

    public SessaoConversacao() {
        super();
    }

    public SessaoConversacao(EnumTipoSessao tipo, LocalDateTime dataHora,
                             EnumModalidadeSessao modalidade, Professor professor) {
        this();
        this.tipoSessao = tipo;
        this.dataHora = dataHora;
        this.modalidadeSessao = modalidade;
        this.professor = professor;
        this.status = EnumStatusSessao.AGENDADA;
        this.maxAlunos = 5;
    }

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

    public EnumTipoSessao getTipoSessao() {
        return tipoSessao;
    }

    public void setTipoSessao(EnumTipoSessao tipoSessao) {
        this.tipoSessao = tipoSessao;
    }

    public void adicionarAluno(Aluno aluno) {
        if (!alunos.contains(aluno) && alunos.size() < maxAlunos) {
            alunos.add(aluno);
        }
    }

    public void removerAluno(Aluno aluno) {
        alunos.remove(aluno);
    }

    public boolean estaCheia() {
        return alunos.size() >= maxAlunos;
    }

    public int vagas() {
        return Math.max(0, maxAlunos - alunos.size());
    }

    @Override
    public String toString() {
        return "SessaoConversacao{" +
                "id=" + id +
                ", tipo=" + tipoSessao +
                ", dataHora=" + dataHora +
                ", status=" + status +
                ", professor=" + (professor != null ? professor.getNome() : "null") +
                ", alunos=" + alunos.size() +
                '}';
    }
}
