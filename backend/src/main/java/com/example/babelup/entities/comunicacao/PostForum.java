package com.example.babelup.entities.comunicacao;

import com.example.babelup.entities.base.EntidadeAuditavel;
import com.example.babelup.entities.usuarios.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "post_forum",indexes = {
        @Index(name = "idx_post_usuario", columnList = "usuario_id"),
        @Index(name = "idx_post_pai", columnList = "post_pai_id"),
        @Index(name = "idx_post_criado", columnList = "criado_em")
})
public class PostForum extends EntidadeAuditavel {

    @NotBlank(message = "Conteúdo não pode estar vazio")
    @Size(min = 1, max = 5000, message = "Conteúdo deve ter entre 1 e 5000 caracteres")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @NotNull
    @Min(value = 0, message = "Curtidas não pode ser negativo")
    @Column(nullable = false)
    private Integer curtidas = 0;

    @NotNull(message = "Usuário não pode ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_pai_id")
    private PostForum postPai;

    @OneToMany(mappedBy = "postPai",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<PostForum> respostas = new ArrayList<>();

    public PostForum() {
        super();
    }

    public PostForum(String conteudo, Usuario usuario) {
        this();
        this.conteudo = conteudo;
        this.usuario = usuario;
        this.curtidas = 0;
    }

    public PostForum(String conteudo, Usuario usuario, PostForum postPai) {
        this(conteudo, usuario);
        this.postPai = postPai;
    }


    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Integer getCurtidas() {
        return curtidas;
    }

    public void setCurtidas(Integer curtidas) {
        this.curtidas = curtidas;
    }

    public void curtir() {
        this.curtidas++;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public PostForum getPostPai() {
        return postPai;
    }

    public void setPostPai(PostForum postPai) {
        this.postPai = postPai;
    }

    public List<PostForum> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<PostForum> respostas) {
        this.respostas = respostas;
    }

    public void addResposta(PostForum resposta) {
        respostas.add(resposta);
        resposta.setPostPai(this);
    }

    public boolean isResposta() {
        return postPai != null;
    }

    @Override
    public String toString() {
        return "PostForum{" +
                "id=" + id +
                ", usuario=" + (usuario != null ? usuario.getEmail() : "null") +
                ", curtidas=" + curtidas +
                ", isResposta=" + isResposta() +
                '}';
    }

}
