package com.example.babelup.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "professor")
public class Professor extends Usuario{
    @Column(name = "idiomas_lecionados",columnDefinition = "TEXT")
    private String idiomasLecionados;

    @Column( columnDefinition = "TEXT")
    private String disponibilidade;


    public String getIdiomasLecionados() {
        return idiomasLecionados;
    }

    public void setIdiomasLecionados(String idiomasLecionados) {
        this.idiomasLecionados = idiomasLecionados;
    }

    public String getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(String disponibilidade) {
        this.disponibilidade = disponibilidade;
    }
}
