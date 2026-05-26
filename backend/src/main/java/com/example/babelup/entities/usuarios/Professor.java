package com.example.babelup.entities.usuarios;

import com.example.babelup.entities.Enum.EnumPerfil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "professor")
@PrimaryKeyJoinColumn(name = "id")
public class Professor extends Usuario{

    @Column(name = "idiomas_lecionados",columnDefinition = "TEXT")
    @Size(max = 1000, message = "Idiomas lecionados deve conter no máximo 1000 caracteres.")
    private String idiomasLecionados;

    @Column( columnDefinition = "TEXT")
    @Size(max = 1000, message = "Disponibilidade deve conter no máximo 1000 caracteres.")
    private String disponibilidade;

    public Professor() {
        super();
    }

    public Professor(String nome, String email, String senha) {
        super(nome, email, senha, EnumPerfil.PROFESSOR);
    }

    public Professor(String nome, String email, String senha, String idiomasLecionados, String disponibilidade) {
        this(nome, email, senha);
        this.idiomasLecionados = idiomasLecionados;
        this.disponibilidade = disponibilidade;
    }



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

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", idiomas='" + idiomasLecionados + '\'' +
                '}';
    }
}
