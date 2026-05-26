package com.example.babelup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateModuloDto {

    @JsonProperty("titulo")
    private String titulo;

    @JsonProperty("descricao")
    private String descricao;

    @JsonProperty("carga_horaria_minima")
    private Integer cargaHorariaMinima;

    @JsonProperty("ordem")
    private Integer ordem;

    public UpdateModuloDto() {
    }

    public UpdateModuloDto(String titulo, String descricao, Integer cargaHorariaMinima, Integer ordem) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.cargaHorariaMinima = cargaHorariaMinima;
        this.ordem = ordem;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getCargaHorariaMinima() {
        return cargaHorariaMinima;
    }

    public void setCargaHorariaMinima(Integer cargaHorariaMinima) {
        this.cargaHorariaMinima = cargaHorariaMinima;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }
}
