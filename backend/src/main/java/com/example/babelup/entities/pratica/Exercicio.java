package com.example.babelup.entities.pratica;

import com.example.babelup.entities.base.EntidadeAuditavel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
@Table(name = "exercicio", indexes = {
        @Index(name = "idx_exercicio_videoaula", columnList = "videoaula_id")
})
public class Exercicio extends EntidadeAuditavel {

    @NotBlank(message = "Enunciado não pode estar vazio")
    @Size(min = 5, max = 2000, message = "Enunciado deve ter entre 5 e 2000 caracteres")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String enunciado;

    @Column(columnDefinition = "JSON")
    private String alternativas;

    @NotBlank(message = "Resposta correta não pode estar vazia")
    @Size(max = 50)
    @Column(name = "resposta_correta", nullable = false,length = 50)
    private String respostaCorreta;

    @NotNull(message = "VideoAula não pode ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "videoaula_id", nullable = false)
    private VideoAula videoAula;

    public Exercicio(){
        super();
    }

    public Exercicio(String enunciado, String respostaCorreta, VideoAula videoAula) {
        this();
        this.enunciado = enunciado;
        this.respostaCorreta = respostaCorreta;
        this.videoAula = videoAula;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(String alternativas) {
        this.alternativas = alternativas;
    }

    public String getRespostaCorreta() {
        return respostaCorreta;
    }

    public void setRespostaCorreta(String respostaCorreta) {
        this.respostaCorreta = respostaCorreta;
    }

    public VideoAula getVideoAula() {
        return videoAula;
    }

    public void setVideoAula(VideoAula videoAula) {
        this.videoAula = videoAula;
    }

    @Override
    public String toString() {
        return "Exercicio{" +
                "id=" + id +
                ", enunciado='" + (enunciado.length() > 50 ? enunciado.substring(0, 50) + "..." : enunciado) + '\'' +
                ", videoAula=" + (videoAula != null ? videoAula.getTitulo() : "null") +
                '}';
    }
}
