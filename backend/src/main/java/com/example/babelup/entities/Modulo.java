package com.example.babelup.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "modulo")
public class Modulo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private Integer ordem;

    @Column(name = "carga_horaria_min")
    private Integer cargaHorariaMinima;

    @ManyToOne
    @JoinColumn(name = "nivel_id", nullable = false)
    private Nivel nivel;

    @OneToMany(mappedBy = "modulo", cascade = CascadeType.ALL)
    private List<VideoAula> videoAulas = new ArrayList<>();

    @OneToMany(mappedBy = "modulo", cascade = CascadeType.ALL)
    private List<MaterialApoio> materiaisApoio = new ArrayList<>();

    @OneToMany(mappedBy = "modulo", cascade = CascadeType.ALL)
    private List<Exercicio> exercicios = new ArrayList<>();

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public Integer getCargaHorariaMinima() {
        return cargaHorariaMinima;
    }

    public void setCargaHorariaMinima(Integer cargaHorariaMinima) {
        this.cargaHorariaMinima = cargaHorariaMinima;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public List<VideoAula> getVideoAulas() {
        return videoAulas;
    }

    public void setVideoAulas(List<VideoAula> videoAulas) {
        this.videoAulas = videoAulas;
    }

    public List<MaterialApoio> getMateriaisApoio() {
        return materiaisApoio;
    }

    public void setMateriaisApoio(List<MaterialApoio> materiaisApoio) {
        this.materiaisApoio = materiaisApoio;
    }

    public List<Exercicio> getExercicios() {
        return exercicios;
    }

    public void setExercicios(List<Exercicio> exercicios) {
        this.exercicios = exercicios;
    }
}
