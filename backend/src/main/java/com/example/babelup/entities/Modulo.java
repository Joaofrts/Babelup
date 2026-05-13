package com.example.babelup.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "modulos")
public class Modulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(name = "url_videoaula")
    private String urlVideoaula;

    @Column(name = "url_pdf")
    private String urlPdf;

    @ManyToOne
    @JoinColumn(name = "nivel_id", nullable = false)
    private Nivel nivel;

    @Column(name = "ordem_sequencial")
    private Integer ordemSequencial;

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

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public Integer getOrdemSequencial() {
        return ordemSequencial;
    }

    public void setOrdemSequencial(Integer ordemSequencial) {
        this.ordemSequencial = ordemSequencial;
    }
}
