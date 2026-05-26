package com.example.babelup.entities.usuarios;

import com.example.babelup.dto.NovoUsuarioDto;
import com.example.babelup.entities.Enum.EnumPerfil;
import com.example.babelup.entities.estruturaAcademica.Nivel;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="aluno",indexes = {
        @Index(name = "idx_aluno_nivel", columnList = "nivel_id"),
        @Index(name = "idx_aluno_progresso", columnList = "progresso_geral")
})
@PrimaryKeyJoinColumn(name = "id")
public class Aluno extends Usuario{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nivel_id")
    private Nivel nivelAtual;

    @NotNull(message = "O progresso geral não pode ser nulo")
    @DecimalMin(value = "0.0",message = "O progresso não pode ser negativo")
    @DecimalMax(value = "100.0", message = "O progresso não pode ser maior que 100")
    @Column(name= "progresso_geral", nullable = false)
    private Double progressoGeral = 0.0;

    @NotNull(message = "Pontuação não pode ser nula")
    @Min(value = 0, message = "A pontuação não pode ser negativa")
    @Column(name="pontuacao_ranking",nullable = false)
    private Integer pontuacaoRanking = 0;

    @NotNull(message = "O aceite dos termos não pode ser nulo")
    @Column(name = "aceite_termos", nullable = false)
    private boolean aceiteTermos;

    @NotNull(message = "Menor de idade não pode ser nulo")
    @Column(name = "menor_idade", nullable = false)
    private boolean menorIdade;

    @Column(name = "dados_responsavel", columnDefinition = "TEXT")
    private String dadosResponsavel;

    public Aluno(){
        super();
    }

    public Aluno(String nome, String email, String senha, String telefone,
                 boolean aceiteTermos, boolean menorIdade) {
        super(nome, email, senha, EnumPerfil.ALUNO);
        this.setTelefone(telefone);
        this.aceiteTermos = aceiteTermos ;
        this.menorIdade = menorIdade;

        this.progressoGeral = 0.0;
        this.pontuacaoRanking = 0;
    }

    public Aluno(NovoUsuarioDto dto, String senhaCriptografada) {
        this(dto.nome(),
                dto.email(),
                senhaCriptografada,
                dto.telefone(),
                dto.aceiteTermos() != null ? dto.aceiteTermos() : false,
                dto.menorIdade() != null ? dto.menorIdade() : false);
    }

    public Nivel getNivelAtual() {
        return nivelAtual;
    }

    public void setNivelAtual(Nivel nivelAtual) {
        this.nivelAtual = nivelAtual;
    }

    public Double getProgressoGeral() {
        return progressoGeral;
    }

    public void setProgressoGeral(Double progressoGeral) {
        this.progressoGeral = progressoGeral;
    }

    public Integer getPontuacaoRanking() {
        return pontuacaoRanking;
    }

    public void setPontuacaoRanking(Integer pontuacaoRanking) {
        this.pontuacaoRanking = pontuacaoRanking;

    }

    public boolean isAceiteTermos() {
        return aceiteTermos;
    }

    public void setAceiteTermos(boolean aceiteTermos) {
        this.aceiteTermos = aceiteTermos;
    }

    public boolean isMenorIdade() {
        return menorIdade;
    }

    public void setMenorIdade(boolean menorIdade) {
        this.menorIdade = menorIdade;
    }

    public String getDadosResponsavel() {
        return dadosResponsavel;
    }

    public void setDadosResponsavel(String dadosResponsavel) {

        this.dadosResponsavel = dadosResponsavel;
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", nivelAtual=" + nivelAtual +
                ", progressoGeral=" + progressoGeral +
                '}';
    }
}
