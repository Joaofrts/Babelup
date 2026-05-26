package com.example.babelup.entities.comunicacao;

import com.example.babelup.entities.base.EntidadeAuditavel;
import com.example.babelup.entities.usuarios.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "mensagem_chat",indexes = {
        @Index(name = "idx_mensagem_remetente", columnList = "remetente_id"),
        @Index(name = "idx_mensagem_destinatario", columnList = "destinatario_id"),
        @Index(name = "idx_mensagem_lida", columnList = "lida"),
        @Index(name = "idx_mensagem_data", columnList = "criado_em")
})
public class MensagemChat extends EntidadeAuditavel {

    @NotBlank(message = "Conteúdo não pode estar vazio")
    @Size(min = 1, max = 5000, message = "Conteúdo deve ter entre 1 e 5000 caracteres")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @NotNull
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean lida = false;

    @NotNull(message = "Remetente não pode ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remetente_id", nullable = false)
    private Usuario remetente;

    @NotNull(message = "Destinatário não pode ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinatario_id", nullable = false)
    private Usuario destinatario;

    public MensagemChat() {
        super();
    }

    public MensagemChat(String conteudo, Usuario remetente, Usuario destinatario) {
        this();
        this.conteudo = conteudo;
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.lida = false;
    }


    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }

    public Usuario getRemetente() {
        return remetente;
    }

    public void setRemetente(Usuario remetente) {
        this.remetente = remetente;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }

    @Override
    public String toString() {
        return "MensagemChat{" +
                "id=" + id +
                ", remetente=" + (remetente != null ? remetente.getEmail() : "null") +
                ", destinatario=" + (destinatario != null ? destinatario.getEmail() : "null") +
                ", lida=" + lida +
                ", criadoEm=" + criadoEm +
                '}';
    }
}
