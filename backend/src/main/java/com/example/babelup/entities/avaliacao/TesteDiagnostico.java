package com.example.babelup.entities.avaliacao;

import com.example.babelup.entities.enumEntities.EnumTipoTeste;
import com.example.babelup.entities.base.EntidadeAuditavel;
import com.example.babelup.entities.estruturaAcademica.Nivel;
import com.example.babelup.entities.usuarios.Aluno;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "teste_diagnostico",indexes = {
        @Index(name = "idx_teste_aluno", columnList = "aluno_id"),
        @Index(name = "idx_teste_nivel", columnList = "nivel_id"),
        @Index(name = "idx_teste_tipo", columnList = "tipo")
})
public class TesteDiagnostico extends EntidadeAuditavel {

    @NotNull(message = "Tipo de teste não pode ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnumTipoTeste tipo;

    @DecimalMin(value = "0.0", message = "Resultado não pode ser negativo")
    @DecimalMax(value = "100.0", message = "Resultado não pode ser maior que 100")
    private Double resultado;

    @Column(name = "parecer_professor", columnDefinition = "TEXT")
    @Size(max = 2000, message = "Parecer do professor deve ter no máximo 2000 caracteres")
    private String parecerProfessor;

    @NotNull(message = "Aluno não pode ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nivel_id")
    private Nivel nivel;

    @NotNull(message = "Sessão de conversação não pode ser nula")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessao_id", nullable = false)
    private SessaoConversacao sessaoConversacao;

    public TesteDiagnostico() {
        super();
    }

    public TesteDiagnostico(EnumTipoTeste tipo, Aluno aluno, SessaoConversacao sessao) {
        this();
        this.tipo = tipo;
        this.aluno = aluno;
        this.sessaoConversacao = sessao;
    }

    public EnumTipoTeste getTipo() {
        return tipo;
    }

    public void setTipo(EnumTipoTeste tipo) {
        this.tipo = tipo;
    }

    public Double getResultado() {
        return resultado;
    }

    public void setResultado(Double resultado) {
        this.resultado = resultado;
    }

    public LocalDateTime getDataRealizacao() {
        return criadoEm;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public String getParecerProfessor() {
        return parecerProfessor;
    }

    public void setParecerProfessor(String parecerProfessor) {
        this.parecerProfessor = parecerProfessor;
    }

    public SessaoConversacao getSessaoConversacao() {
        return sessaoConversacao;
    }

    public void setSessaoConversacao(SessaoConversacao sessaoConversacao) {
        this.sessaoConversacao = sessaoConversacao;
    }

    public boolean isConcluido() {
        return resultado != null;
    }

    @Override
    public String toString() {
        return "TesteDiagnostico{" +
                "id=" + id +
                ", tipo=" + tipo +
                ", resultado=" + resultado +
                ", aluno=" + (aluno != null ? aluno.getNome() : "null") +
                '}';
    }
}
