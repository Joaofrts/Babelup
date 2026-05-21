package com.example.babelup.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "matricula")
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "data_inicio", nullable = false,updatable = false)
    private LocalDate dataInicio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumStatusMatricula status;

    @Column(nullable = false,length = 50)
    private String idioma;

    @Column(length = 50)
    private String plano;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @PrePersist
    protected void onCreate(){
        if(this.dataInicio==null){
            this.dataInicio = LocalDate.now();
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public EnumStatusMatricula getStatus() {
        return status;
    }

    public void setStatus(EnumStatusMatricula status) {
        this.status = status;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getPlano() {
        return plano;
    }

    public void setPlano(String plano) {
        this.plano = plano;
    }

    public Aluno getUsuario() {
        return aluno;
    }

    public void setUsuario(Aluno aluno) {
        this.aluno = aluno;
    }
}
