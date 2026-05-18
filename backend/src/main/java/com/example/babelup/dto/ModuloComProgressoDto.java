package com.example.babelup.dto;

import com.example.babelup.entities.Modulo;
import com.example.babelup.entities.Progresso;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ModuloComProgressoDto {

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

    private ProgressoDto progresso;

    @JsonProperty("pode_acessar")
    private Boolean podeAcessar;

    public ModuloComProgressoDto() {
    }

    public ModuloComProgressoDto(Modulo modulo, Progresso progresso, Boolean podeAcessar) {
        this.id = modulo.getId();
        this.titulo = modulo.getTitulo();
        this.urlVideoaula = modulo.getUrlVideoaula();
        this.urlPdf = modulo.getUrlPdf();
        this.nivelId = modulo.getNivel() != null ? modulo.getNivel().getId() : null;
        this.ordemSequencial = modulo.getOrdemSequencial();
        this.progresso = progresso != null ? new ProgressoDto(progresso) : null;
        this.podeAcessar = podeAcessar;
    }

    public ModuloComProgressoDto(Modulo modulo, Boolean podeAcessar) {
        this(modulo, null, podeAcessar);
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

    public ProgressoDto getProgresso() {
        return progresso;
    }

    public void setProgresso(ProgressoDto progresso) {
        this.progresso = progresso;
    }

    public Boolean getPodeAcessar() {
        return podeAcessar;
    }

    public void setPodeAcessar(Boolean podeAcessar) {
        this.podeAcessar = podeAcessar;
    }
}
