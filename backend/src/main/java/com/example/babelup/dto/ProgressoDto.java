package com.example.babelup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class ProgressoDto {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("aluno_id")
    private Long alunoId;
    
    @JsonProperty("modulo_id")
    private Long moduloId;
    
    @JsonProperty("exercicio_concluido")
    private Boolean exercicioConcluido;
    
    @JsonProperty("nota_exercicio")
    private Double notaExercicio;
    
    @JsonProperty("data_conclusao")
    private LocalDateTime dataConclusao;

    public ProgressoDto() {}

    public ProgressoDto(Long id, Long alunoId, Long moduloId, Boolean exercicioConcluido, Double notaExercicio, LocalDateTime dataConclusao) {
        this.id = id;
        this.alunoId = alunoId;
        this.moduloId = moduloId;
        this.exercicioConcluido = exercicioConcluido;
        this.notaExercicio = notaExercicio;
        this.dataConclusao = dataConclusao;
    }

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
