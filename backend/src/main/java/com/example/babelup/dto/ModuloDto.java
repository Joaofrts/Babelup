package com.example.babelup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModuloDto {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("titulo")
    private String titulo;
    
    @JsonProperty("url_videoaula")
    private String urlVideoaula;
    
    @JsonProperty("url_pdf")
    private String urlPdf;
    
    @JsonProperty("nivel_id")
    private Long nivelId;
    
    @JsonProperty("ordem_sequencial")
    private Integer ordemSequencial;

    public ModuloDto() {}

    public ModuloDto(Long id, String titulo, String urlVideoaula, String urlPdf, Long nivelId, Integer ordemSequencial) {
        this.id = id;
        this.titulo = titulo;
        this.urlVideoaula = urlVideoaula;
        this.urlPdf = urlPdf;
        this.nivelId = nivelId;
        this.ordemSequencial = ordemSequencial;
    }

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
