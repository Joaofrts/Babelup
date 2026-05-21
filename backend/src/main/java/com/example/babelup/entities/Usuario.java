package com.example.babelup.entities;

import com.example.babelup.dto.UsuarioDto;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name= "senha_hash",nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil",nullable = false, length= 20)
    private EnumPerfil enumPerfil;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @PrePersist
    protected void onCreate() {
        this.dataCadastro = LocalDateTime.now();
    }

    public Usuario() {}

    public Usuario(UUID id, String nome, String email, String senha, EnumPerfil enumPerfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.enumPerfil = enumPerfil;
    }
    public UUID getId() {
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

    public EnumPerfil getPerfil() {
        return enumPerfil;
    }

    public void setPerfil(EnumPerfil enumPerfil) {
        this.enumPerfil = enumPerfil;
    }
}
