package com.example.babelup.entities;

import com.example.babelup.dto.UsuarioDto;
import jakarta.persistence.*;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
    private String email;
    private String senha;
    @Enumerated(EnumType.STRING)
    private Perfil perfil;




    public Usuario() {
    }

    public Usuario(Long id, String nome, String email, String senha, Perfil perfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
    }
    public Usuario(UsuarioDto dto){
        this.id = dto.id;
        this.nome = dto.nome;
        this.email = dto.email;
        this.senha = dto.senha;
        this.perfil = dto.perfil;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}
