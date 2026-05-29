package com.example.babelup.entities.usuarios;

import com.example.babelup.entities.enumEntities.EnumPerfil;
import com.example.babelup.entities.base.EntidadeAuditavel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
@Table(name = "usuario",indexes = {
        @Index(name = "idx_usuario_email", columnList = "email"),
        @Index(name = "idx_usuario_perfil", columnList = "perfil")
})
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario extends EntidadeAuditavel {

    @NotBlank(message = "Nome não pode ser vazio")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Column(nullable = false, length = 100)
    protected String nome;

    @NotBlank(message = "Email não pode ser vazio")
    @Email(message = "Email inválido")
    @Column(nullable = false, unique = true, length = 100)
    protected String email;


    @Column(length = 20)
    protected String telefone;

    @NotBlank(message = "Senha não pode ser vazia")
    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    @Column(name= "senha_hash",nullable = false,length = 255)
    private String senha;

    @NotNull(message = "Perfil é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "perfil",nullable = false, length= 20)
    private EnumPerfil enumPerfil;


    public Usuario() {
        super();
    }


    public Usuario(UUID id, String nome, String email, String senha, EnumPerfil enumPerfil) {
        super(id);
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.enumPerfil = enumPerfil;
    }

    public Usuario(String nome, String email, String senha, EnumPerfil enumPerfil) {
        super();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.enumPerfil = enumPerfil;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
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

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", enumPerfil=" + enumPerfil +
                '}';

    }
}
