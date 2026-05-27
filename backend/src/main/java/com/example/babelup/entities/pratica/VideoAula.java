package com.example.babelup.entities.pratica;

import com.example.babelup.entities.enumEntities.EnumTipoVideo;
import com.example.babelup.entities.base.EntidadeAuditavel;
import com.example.babelup.entities.estruturaAcademica.Modulo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "videoaula", indexes = {
        @Index(name = "idx_video_modulo", columnList = "modulo_id"),
        @Index(name = "idx_video_tipo", columnList = "tipo")
})
public class VideoAula extends EntidadeAuditavel {

    @NotBlank(message = "Titulo não pode estar vazio")
    @Size(max = 100, min = 3,message = "Titulo deve conter entre 3 e 100 caracteres")
    @Column(nullable = false,length = 100)
    private String titulo;

    @NotBlank(message = "URL não pode estar vazia")
    @Size(max = 2000,message = "URL deve conter no máximo 2000 caracteres")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String url;

    @Column(name = "duracao")
    @Positive(message = "Duração deve ser positiva")
    private Integer duracao;

    @NotNull(message = "Tipo não pode ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnumTipoVideo tipo;

    @NotNull(message = "Modulo não pode ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modulo_id", nullable = false)
    private Modulo modulo;

    @OneToMany(mappedBy = "videoAula",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Exercicio> exercicios = new ArrayList<>();

    public VideoAula(){
        super();
    }

    public VideoAula(String titulo, String url, EnumTipoVideo tipo, Modulo modulo) {
        this();
        this.titulo = titulo;
        this.url = url;
        this.tipo = tipo;
        this.modulo = modulo;
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

    public LocalDateTime getDataTransmissao() {
        return criadoEm;
    }

    public List<Exercicio> getExercicios() {
        return exercicios;
    }

    public void setExercicios(List<Exercicio> exercicios) {
        this.exercicios = exercicios;
    }

    public void adicionarExercicio(Exercicio exercicio) {
        exercicios.add(exercicio);
        exercicio.setVideoAula(this);
    }

    public String getDuracaoFormatada() {
        if (duracao == null) return "N/A";
        int horas = duracao / 60;
        int minutos = duracao % 60;
        return String.format("%02d:%02d", horas, minutos);
    }

    @Override
    public String toString() {
        return "VideoAula{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", tipo=" + tipo +
                ", duracao=" + getDuracaoFormatada() +
                '}';
    }

}
