package com.example.babelup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NivelDto {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("nome")
    private String nome;
    
    @JsonProperty("carga_horaria_estimada")
    private Integer cargaHorariaEstimada;

    public NivelDto() {}

    public NivelDto(Long id, String nome, Integer cargaHorariaEstimada) {
        this.id = id;
        this.nome = nome;
        this.cargaHorariaEstimada = cargaHorariaEstimada;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
