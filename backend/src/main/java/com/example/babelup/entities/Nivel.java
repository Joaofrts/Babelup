package com.example.babelup.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "niveis")
public class Nivel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(name = "carga_horaria_estimada")
    private Integer cargaHorariaEstimada;

    @OneToMany(mappedBy = "nivel", cascade = CascadeType.ALL)
    private List<Modulo> modulos;

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

    public List<Modulo> getModulos() {
        return modulos;
    }

    public void setModulos(List<Modulo> modulos) {
        this.modulos = modulos;
    }
}
