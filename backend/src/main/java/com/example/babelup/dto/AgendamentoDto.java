package com.example.babelup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

public class AgendamentoDto {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("data_hora")
    private LocalDateTime dataHora;
    
    @JsonProperty("professor_id")
    private Long professorId;
    
    @JsonProperty("modulo_id")
    private Long moduloId;
    
    @JsonProperty("aluno_ids")
    private List<Long> alunoIds;
    
    @JsonProperty("quantidade_alunos")
    private Integer quantidadeAlunos;

    public AgendamentoDto() {}

    public AgendamentoDto(Long id, LocalDateTime dataHora, Long professorId, Long moduloId, List<Long> alunoIds, Integer quantidadeAlunos) {
        this.id = id;
        this.dataHora = dataHora;
        this.professorId = professorId;
        this.moduloId = moduloId;
        this.alunoIds = alunoIds;
        this.quantidadeAlunos = quantidadeAlunos;
    }

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

    public Long getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Long professorId) {
        this.professorId = professorId;
    }

    public Long getModuloId() {
        return moduloId;
    }

    public void setModuloId(Long moduloId) {
        this.moduloId = moduloId;
    }

    public List<Long> getAlunoIds() {
        return alunoIds;
    }

    public void setAlunoIds(List<Long> alunoIds) {
        this.alunoIds = alunoIds;
    }

    public Integer getQuantidadeAlunos() {
        return quantidadeAlunos;
    }

    public void setQuantidadeAlunos(Integer quantidadeAlunos) {
        this.quantidadeAlunos = quantidadeAlunos;
    }
}
