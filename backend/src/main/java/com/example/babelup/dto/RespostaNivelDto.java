package com.example.babelup.dto;

import com.example.babelup.entities.estruturaAcademica.Nivel;

import java.util.List;
import java.util.UUID;

public class RespostaNivelDto{
        private UUID id;
        private String idioma;
        private String nome;
        private Integer cargaHorariaEstimada;
        private String descricao;
        private List<RespostaModuloDto> modulos;
        private Integer ordem;

        public RespostaNivelDto(Nivel nivel) {
            this.id = nivel.getId();
            this.idioma = nivel.getIdioma();
            this.nome = nivel.getNome();
            this.cargaHorariaEstimada = nivel.getCargaHoraria();
            this.descricao = nivel.getDescricao();
            this.modulos = nivel.getModulos().stream().map(RespostaModuloDto::new).toList();
            this.ordem = nivel.getOrdem();
        }

    public UUID getId() {
        return id;
    }

    public String getIdioma() {
        return idioma;
    }

    public String getNome() {
        return nome;
    }

    public Integer getCargaHorariaEstimada() {
        return cargaHorariaEstimada;
    }

    public String getDescricao() {
        return descricao;
    }

    public List<RespostaModuloDto> getModulos() {
        return modulos;
    }

    public Integer getOrdem() {
        return ordem;
    }
}
