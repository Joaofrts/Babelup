package com.example.babelup.dto;

import com.example.babelup.entities.Progresso;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class ProgressoDto {

    private Long id;

    @JsonProperty("aluno_id")
    private Long alunoId;

    @JsonProperty("modulo_id")
    private Long moduloId;

    @JsonProperty("modulo_titulo")
    private String moduloTitulo;

    @JsonProperty("exercicio_concluido")
    private Boolean exercicioConcluido;

    @JsonProperty("nota_exercicio")
    private Double notaExercicio;

    @JsonProperty("data_conclusao")
    private LocalDateTime dataConclusao;

    public ProgressoDto() {
    }

    public ProgressoDto(Progresso progresso) {
        this.id = progresso.getId();
        this.alunoId = progresso.getAluno() != null ? progresso.getAluno().getId() : null;
        this.moduloId = progresso.getModulo() != null ? progresso.getModulo().getId() : null;
        this.moduloTitulo = progresso.getModulo() != null ? progresso.getModulo().getTitulo() : null;
        this.exercicioConcluido = progresso.getExercicioConcluido();
        this.notaExercicio = progresso.getNotaExercicio();
        this.dataConclusao = progresso.getDataConclusao();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public Long getModuloId() {
        return moduloId;
    }

    public void setModuloId(Long moduloId) {
        this.moduloId = moduloId;
    }

    public String getModuloTitulo() {
        return moduloTitulo;
    }

    public void setModuloTitulo(String moduloTitulo) {
        this.moduloTitulo = moduloTitulo;
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
