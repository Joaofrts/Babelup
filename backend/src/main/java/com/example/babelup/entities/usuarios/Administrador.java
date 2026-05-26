package com.example.babelup.entities.usuarios;

import com.example.babelup.dto.NovoUsuarioDto;
import com.example.babelup.entities.Enum.EnumPerfil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

@Entity
@Table(name = "administrador")
@PrimaryKeyJoinColumn(name = "id")
public class Administrador extends Usuario{

    @Column(name = "nivel_acesso", length = 20)
    @Pattern(regexp = "SUPER_ADMIN|ADMIN|MODERADOR",
            message = "Nivel de acesso deve ser: SUPER_ADMIN, ADMIN ou MODERADOR")
    private String nivelAcesso;

    public Administrador() {
        super();
    }

    public Administrador(String nome, String email, String senha) {
        super(nome, email, senha, EnumPerfil.ADMIN);
        this.nivelAcesso = "ADMIN";
    }

    public Administrador(String nome, String email, String senha, String nivelAcesso) {
        this(nome, email, senha);
        this.nivelAcesso = nivelAcesso;
    }

    public Administrador(NovoUsuarioDto dto, String senhaCriptografada) {
        this(dto.nome(), dto.email(), senhaCriptografada, "ADMIN");
        if (dto.telefone() != null && !dto.telefone().isEmpty()) {
            this.setTelefone(dto.telefone());
        }
    }


    public String getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(String nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }

    public boolean isSuperAdmin() {
        return "SUPER_ADMIN".equals(nivelAcesso);
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", nivelAcesso='" + nivelAcesso + '\'' +
                '}';
    }
}
