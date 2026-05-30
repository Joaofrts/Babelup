package com.example.babelup.entities.estruturaAcademica;

import com.example.babelup.dto.AdicionarNivelDto;
import com.example.babelup.entities.base.EntidadeAuditavel;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "nivel", indexes = {
        @Index(name = "idx_nivel_nome", columnList = "nome"),
        @Index(name = "idx_nivel_idioma", columnList = "idioma")
})
public class Nivel extends EntidadeAuditavel {

    @NotBlank(message = "O idioma não pode estar vazio")
    @Size(min = 2,max = 50)
    @Column(nullable = false, length = 50)
    private String idioma;

    @NotBlank(message = "O nome do nível não pode estar vazio")
    @Size(min = 2, max = 50)
    @Column(nullable = false, length = 50)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "carga_horaria")
    @Positive(message = "A carga horária deve ser um número positivo")
    private Integer cargaHoraria;

    @NotNull(message = "A ordem não pode ser nula")
    @Positive(message = "A ordem deve ser positiva")
    @Column(nullable = false)
    private Integer ordem;

    @Column(name = "preco_mensal", precision = 10, scale = 2)
    @DecimalMin(value = "0.00", inclusive = true, message = "Preço mensal deve ser positivo ou zero")
    private BigDecimal precoMensal;

    @OneToMany(
            mappedBy = "nivel",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Modulo> modulos = new ArrayList<>();

    public Nivel() {
        super();
    }

    public Nivel(String idioma, String nome, Integer ordem) {
        this();
        this.idioma = idioma;
        this.nome = nome;
        this.ordem = ordem;
    }

    public Nivel(String idioma, String nome, Integer ordem, String descricao, BigDecimal precoMensal, Integer cargaHoraria) {
        this(idioma, nome, ordem);
        this.descricao = descricao;
        this.precoMensal = precoMensal;
        this.cargaHoraria = cargaHoraria;
    }

    public Nivel(AdicionarNivelDto dto) {
        this(dto.idioma(), dto.nome(), dto.ordem(), dto.descricao(), dto.precoMensal(), dto.cargaHoraria());
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPrecoMensal() {
        return precoMensal;
    }

    public void setPrecoMensal(BigDecimal precoMensal) {
        this.precoMensal = precoMensal;
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public List<Modulo> getModulos() {
        return modulos;
    }

    public void setModulos(List<Modulo> modulos) {
        this.modulos = modulos;
    }

    public void addModulo(Modulo modulo) {
        modulos.add(modulo);
        modulo.setNivel(this);
    }

    public void removeModulo(Modulo modulo) {
        modulos.remove(modulo);
        modulo.setNivel(null);
    }

    @Override
    public String toString() {
        return "Nivel{" +
                "id=" + id +
                ", idioma='" + idioma + '\'' +
                ", nome='" + nome + '\'' +
                ", ordem=" + ordem +
                '}';
    }
}


