package com.example.babelup.entities.comunicacao;

import com.example.babelup.entities.Enum.EnumTipoNotificacao;
import com.example.babelup.entities.base.EntidadeAuditavel;
import com.example.babelup.entities.usuarios.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
@Table(name = "notificacao", indexes = {
        @Index(name = "idx_notificacao_usuario", columnList = "usuario_id"),
        @Index(name = "idx_notificacao_lida", columnList = "lida"),
        @Index(name = "idx_notificacao_tipo", columnList = "tipo")
})
public class Notificacao extends EntidadeAuditavel {

    @NotNull(message = "Tipo de notificação não pode ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private EnumTipoNotificacao tipo;

    @NotBlank(message = "Mensagem da notificação não pode ser vazia")
    @Size(min =1, max = 1000)
    @Column(nullable = false,columnDefinition = "TEXT")
    private String mensagem;

    @NotNull
    @Column(nullable = false)
    private boolean lida = false;

    @NotNull(message = "Usuário associado à notificação não pode ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id",nullable = false)
    private Usuario usuario;

    public Notificacao() {
        super();
    }

    public Notificacao(EnumTipoNotificacao tipo, String mensagem, Usuario usuario) {
        this();
        this.tipo = tipo;
        this.mensagem = mensagem;
        this.usuario = usuario;
        this.lida = false;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public EnumTipoNotificacao getTipo() {
        return tipo;
    }

    public void setTipo(EnumTipoNotificacao tipo) {
        this.tipo = tipo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Notificacao{" +
                "id=" + id +
                ", tipo=" + tipo +
                ", lida=" + lida +
                ", usuario=" + (usuario != null ? usuario.getEmail() : "null") +
                '}';
    }
}
