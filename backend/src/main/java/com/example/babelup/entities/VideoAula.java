package com.example.babelup.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "videoaula")
public class VideoAula {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,length = 100)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String url;

    private Integer duracao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnumTipoVideo tipo;

    @ManyToOne
    @JoinColumn(name = "modulo_id", nullable = false)
    private Modulo modulo;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getDuracao() {
        return duracao;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    public EnumTipoVideo getTipo() {
        return tipo;
    }

    public void setTipo(EnumTipoVideo tipo) {
        this.tipo = tipo;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }
}
