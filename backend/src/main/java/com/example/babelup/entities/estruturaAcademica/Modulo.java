package com.example.babelup.entities.estruturaAcademica;

import com.example.babelup.entities.base.EntidadeAuditavel;
import com.example.babelup.entities.pratica.MaterialApoio;
import com.example.babelup.entities.pratica.VideoAula;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "modulo", indexes = {
        @Index(name = "idx_modulo_ordem", columnList = "ordem"),
        @Index(name = "idx_modulo_nivel", columnList = "nivel_id")
})
public class Modulo extends EntidadeAuditavel {

    @NotBlank(message = "Titulo não pode ser vazio")
    @Size(min = 3, max = 100, message = "Titulo deve ter entre 3 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @NotNull(message = "Ordem não pode ser nula")
    @Positive(message = "Ordem deve ser positiva")
    @Column(nullable = false)
    private Integer ordem;


    @Column(name = "carga_horaria_min")
    @PositiveOrZero(message = "Carga horária mínima não pode ser negativa")
    private Integer cargaHorariaMinima;

    @Column(name = "preco_mensal", precision = 10, scale = 2)
    @DecimalMin(value = "0.00", inclusive = true, message = "Preço mensal deve ser positivo ou zero")
    private BigDecimal precoMensal;

    @NotNull(message = "Nivel não pode ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nivel_id", nullable = false)
    private Nivel nivel;

    @OneToMany(
            mappedBy = "modulo",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<VideoAula> videoAulas = new ArrayList<>();

    @OneToMany(
            mappedBy = "modulo",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<MaterialApoio> materiaisApoio = new ArrayList<>();

    public Modulo() {
        super();
    }

    public Modulo(String titulo, Integer ordem, Nivel nivel) {
        this();
        this.titulo = titulo;
        this.ordem = ordem;
        this.nivel = nivel;
    }

    public Modulo(String titulo, String descricao, Integer ordem, Integer cargaHorariaMinima, Nivel nivel, BigDecimal precoMensal) {
        this(titulo, ordem, nivel);
        this.descricao = descricao;
        this.cargaHorariaMinima = cargaHorariaMinima;
        this.precoMensal = precoMensal;

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

    public BigDecimal getPrecoMensal() {
        return precoMensal;
    }

    public void setPrecoMensal(BigDecimal precoMensal) {
        this.precoMensal = precoMensal;
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

    public void addVideoAula(VideoAula videoAula) {
        videoAulas.add(videoAula);
        videoAula.setModulo(this);
    }

    public void addMaterialApoio(MaterialApoio material) {
        materiaisApoio.add(material);
        material.setModulo(this);
    }

    @Override
    public String toString() {
        return "Modulo{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", ordem=" + ordem +
                ", nivelId=" + (nivel != null ? nivel.getId() : null) +
                '}';
    }
}
