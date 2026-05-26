package com.example.babelup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class NivelDto {
    
    @JsonProperty("id")
    private UUID id;
    
    @JsonProperty("nome")
    private String nome;
    
    @JsonProperty("carga_horaria_estimada")
    private Integer cargaHorariaEstimada;

    public NivelDto() {}

    public NivelDto(UUID id, String nome, Integer cargaHorariaEstimada) {
        this.id = id;
        this.nome = nome;
        this.cargaHorariaEstimada = cargaHorariaEstimada;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCargaHorariaEstimada() {
        return cargaHorariaEstimada;
    }

    public void setCargaHorariaEstimada(Integer cargaHorariaEstimada) {
        this.cargaHorariaEstimada = cargaHorariaEstimada;
    }
}
