package com.example.babelup.entities.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

import java.util.UUID;

@MappedSuperclass
public abstract class EntidadeAuditavel extends EntidadeBase{
    @Column(name = "criado_por", length = 100)
    protected String criadoPor;

    @Column(name = "atualizado_por", length = 100)
    protected String atualizadoPor;

    @Version
    @Column(name= "versao")
    protected Long versao;

    public EntidadeAuditavel() {
        super();
    }

    public EntidadeAuditavel(UUID id) {
        super(id);
    }

    public String getCriadoPor() {
        return criadoPor;
    }

    public void setCriadoPor(String criadoPor) {
        this.criadoPor = criadoPor;
    }

    public String getAtualizadoPor() {
        return atualizadoPor;
    }

    public void setAtualizadoPor(String atualizadoPor) {
        this.atualizadoPor = atualizadoPor;
    }

    public Long getVersao() {
        return versao;
    }

    public void setVersao(Long versao) {
        this.versao = versao;
    }
}
