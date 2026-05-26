package com.example.babelup.entities.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreRemove;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
public abstract class SoftDeleteEntity extends EntidadeAuditavel{
    @Column(name = "deletado_em")
    protected LocalDateTime deletadoEm;

    public SoftDeleteEntity() {
    }

    public SoftDeleteEntity(UUID id) {
        super(id);
    }

    @PreRemove
    protected void onDelete(){
        this.deletadoEm = LocalDateTime.now();
    }

    public LocalDateTime getDeletadoEm() {
        return deletadoEm;
    }

    public void setDeletadoEm(LocalDateTime deletadoEm) {
        this.deletadoEm = deletadoEm;
    }

    public boolean isAtivo(){
        return this.deletadoEm == null;
    }
}
