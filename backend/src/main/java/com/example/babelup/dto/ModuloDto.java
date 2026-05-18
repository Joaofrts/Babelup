package com.example.babelup.dto;

import com.example.babelup.entities.Modulo;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ModuloDto {

    private Long id;
    private String titulo;

    @JsonProperty("url_videoaula")
    private String urlVideoaula;

    @JsonProperty("url_pdf")
    private String urlPdf;

    @JsonProperty("nivel_id")
    private Long nivelId;

    @JsonProperty("ordem_sequencial")
    private Integer ordemSequencial;

    public ModuloDto() {
    }

    public ModuloDto(Modulo modulo) {
        this.id = modulo.getId();
        this.titulo = modulo.getTitulo();
        this.urlVideoaula = modulo.getUrlVideoaula();
        this.urlPdf = modulo.getUrlPdf();
        this.nivelId = modulo.getNivel() != null ? modulo.getNivel().getId() : null;
        this.ordemSequencial = modulo.getOrdemSequencial();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrlVideoaula() {
        return urlVideoaula;
    }

    public void setUrlVideoaula(String urlVideoaula) {
        this.urlVideoaula = urlVideoaula;
    }

    public String getUrlPdf() {
        return urlPdf;
    }

    public void setUrlPdf(String urlPdf) {
        this.urlPdf = urlPdf;
    }

    public Long getNivelId() {
        return nivelId;
    }

    public void setNivelId(Long nivelId) {
        this.nivelId = nivelId;
    }

    public Integer getOrdemSequencial() {
        return ordemSequencial;
    }

    public void setOrdemSequencial(Integer ordemSequencial) {
        this.ordemSequencial = ordemSequencial;
    }
}
