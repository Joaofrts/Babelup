package com.example.babelup.dto;

import com.example.babelup.entities.Enum.EnumStatusProgresso;
import com.example.babelup.entities.progressoGamificacao.ProgressoAluno;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;

public class RespostaProgressoDto {
    
    @JsonProperty("id")
    private UUID id;
    
    @JsonProperty("aluno_id")
    private UUID alunoId;
    
    @JsonProperty("modulo_id")
    private UUID moduloId;
    
    @JsonProperty("exercicio_concluido")
    private Boolean exercicioConcluido;
    
    @JsonProperty("nota_exercicio")
    private Double notaExercicio;
    
    @JsonProperty("data_conclusao")
    private LocalDateTime dataConclusao;

    public RespostaProgressoDto() {}

    public RespostaProgressoDto(UUID id, UUID alunoId, UUID moduloId, Boolean exercicioConcluido, Double notaExercicio, LocalDateTime dataConclusao) {
        this.id = id;
        this.alunoId = alunoId;
        this.moduloId = moduloId;
        this.exercicioConcluido = exercicioConcluido;
        this.notaExercicio = notaExercicio;
        this.dataConclusao = dataConclusao;
    }

    public RespostaProgressoDto(ProgressoAluno progressoAluno){
        this.id = progressoAluno.getId();
        this.alunoId = progressoAluno.getAluno().getId();
        this.moduloId = progressoAluno.getModulo().getId();
        this.exercicioConcluido = progressoAluno.getStatus() == EnumStatusProgresso.CONCLUIDO;
        this.notaExercicio = progressoAluno.getPercentualConcluido();
        this.dataConclusao = progressoAluno.getUltimoAcesso();
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(UUID alunoId) {
        this.alunoId = alunoId;
    }

    public UUID getModuloId() {
        return moduloId;
    }

    public void setModuloId(UUID moduloId) {
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
