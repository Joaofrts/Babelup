package com.example.babelup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AgendamentoDto {
    
    @JsonProperty("id")
    private UUID id;
    
    @JsonProperty("data_hora")
    private LocalDateTime dataHora;
    
    @JsonProperty("professor_id")
    private UUID professorId;
    
    @JsonProperty("modulo_id")
    private UUID moduloId;
    
    @JsonProperty("aluno_ids")
    private List<UUID> alunoIds;
    
    @JsonProperty("quantidade_alunos")
    private Integer quantidadeAlunos;

    public AgendamentoDto() {}

    public AgendamentoDto(UUID id, LocalDateTime dataHora, UUID professorId, UUID moduloId, List<UUID> alunoIds, Integer quantidadeAlunos) {
        this.id = id;
        this.dataHora = dataHora;
        this.professorId = professorId;
        this.moduloId = moduloId;
        this.alunoIds = alunoIds;
        this.quantidadeAlunos = quantidadeAlunos;
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

    public UUID getProfessorId() {
        return professorId;
    }

    public void setProfessorId(UUID professorId) {
        this.professorId = professorId;
    }

    public UUID getModuloId() {
        return moduloId;
    }

    public void setModuloId(UUID moduloId) {
        this.moduloId = moduloId;
    }

    public List<UUID> getAlunoIds() {
        return alunoIds;
    }

    public void setAlunoIds(List<UUID> alunoIds) {
        this.alunoIds = alunoIds;
    }

    public Integer getQuantidadeAlunos() {
        return quantidadeAlunos;
    }

    public void setQuantidadeAlunos(Integer quantidadeAlunos) {
        this.quantidadeAlunos = quantidadeAlunos;
    }
}
