package com.example.babelup.entities.pratica;

import com.example.babelup.entities.base.EntidadeAuditavel;
import com.example.babelup.entities.estruturaAcademica.Modulo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "material_apoio", indexes = {
        @Index(name = "idx_material_modulo", columnList = "modulo_id")
})
public class MaterialApoio extends EntidadeAuditavel {

    @NotBlank(message = "Titulo não pode estar vazio")
    @Size(min = 3, max = 100, message = "Titulo deve ter entre 3 e 100 caracteres")
    @Column(nullable = false,length = 100)
    private String titulo;

    @NotBlank(message = "URL do PDF não pode estar vazio")
    @Size(max = 2000,message = "URL do PDF deve ter no máximo 2000 caracteres")
    @Column(name = "url_pdf", nullable = false,columnDefinition = "TEXT")
    private String urlPdf;

    @NotNull(message = "Modulo não pode ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modulo_id", nullable = false)
    private Modulo modulo;

    public MaterialApoio() {
        super();
    }

    public MaterialApoio(String titulo, String urlPdf, Modulo modulo) {
        this();
        this.titulo = titulo;
        this.urlPdf = urlPdf;
        this.modulo = modulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrlPdf() {
        return urlPdf;
    }

    public void setUrlPdf(String urlPdf) {
        this.urlPdf = urlPdf;
    }

    public LocalDateTime getDataUpload() {
        return criadoEm;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    @Override
    public String toString() {
        return "MaterialApoio{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", modulo=" + (modulo != null ? modulo.getTitulo() : "null") +
                '}';
    }
}
