package com.example.babelup.dto;

import com.example.babelup.entities.Agendamento;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AgendamentoDto {

    private Long id;

    @JsonProperty("data_hora")
    private LocalDateTime dataHora;

    @JsonProperty("professor_id")
    private Long professorId;

    @JsonProperty("professor_nome")
    private String professorNome;

    @JsonProperty("modulo_id")
    private Long moduloId;

    @JsonProperty("modulo_titulo")
    private String moduloTitulo;

    @JsonProperty("alunos_ids")
    private List<Long> alunosIds;

    @JsonProperty("quantidade_alunos")
    private Integer quantidadeAlunos;

    public AgendamentoDto() {
    }

    public AgendamentoDto(Agendamento agendamento) {
        this.id = agendamento.getId();
        this.dataHora = agendamento.getDataHora();
        this.professorId = agendamento.getProfessor() != null ? agendamento.getProfessor().getId() : null;
        this.professorNome = agendamento.getProfessor() != null ? agendamento.getProfessor().getNome() : null;
        this.moduloId = agendamento.getModulo() != null ? agendamento.getModulo().getId() : null;
        this.moduloTitulo = agendamento.getModulo() != null ? agendamento.getModulo().getTitulo() : null;
        this.alunosIds = agendamento.getAlunos() != null
                ? agendamento.getAlunos().stream().map(u -> u.getId()).collect(Collectors.toList())
                : List.of();
        this.quantidadeAlunos = agendamento.getAlunos() != null ? agendamento.getAlunos().size() : 0;
    }

    // Getters and Setters
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

    public String getProfessorNome() {
        return professorNome;
    }

    public void setProfessorNome(String professorNome) {
        this.professorNome = professorNome;
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

    public List<Long> getAlunosIds() {
        return alunosIds;
    }

    public void setAlunosIds(List<Long> alunosIds) {
        this.alunosIds = alunosIds;
    }

    public Integer getQuantidadeAlunos() {
        return quantidadeAlunos;
    }

    public void setQuantidadeAlunos(Integer quantidadeAlunos) {
        this.quantidadeAlunos = quantidadeAlunos;
    }
}
