package com.example.babelup.dto;

import com.example.babelup.entities.estruturaAcademica.Modulo;
import com.example.babelup.entities.pratica.MaterialApoio;
import com.example.babelup.entities.pratica.VideoAula;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class RespostaModuloDto {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("titulo")
    private String titulo;

    @JsonProperty("descricao")
    private String descricao;

    @JsonProperty("videoaulas")
    private List<VideoAula> videoAulas;

    @JsonProperty("url_pdf")
    private List<MaterialApoio> materialApoios;

    @JsonProperty("nivel_id")
    private UUID nivelId;

    @JsonProperty("carga_horaria_minima")
    private Integer cargaHorariaMinima;

    @JsonProperty("ordem")
    private Integer ordem;

    public RespostaModuloDto() {}

    public RespostaModuloDto(Modulo modulo){
        this.id = modulo.getId();
        this.titulo = modulo.getTitulo();
        this.descricao = modulo.getDescricao();
        this.videoAulas = modulo.getVideoAulas();
        this.materialApoios = modulo.getMateriaisApoio();
        this.nivelId = modulo.getNivel().getId();
        this.cargaHorariaMinima = modulo.getCargaHorariaMinima();
        this.ordem = modulo.getOrdem();
    }

    public RespostaModuloDto(UUID id, String titulo, String descricao, List<VideoAula> videoAulas, List<MaterialApoio> materialApoios, UUID nivelId, Integer ordem) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.videoAulas = videoAulas;
        this.materialApoios = materialApoios;
        this.nivelId = nivelId;
        this.ordem = ordem;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<VideoAula> getVideoAulas() {
        return videoAulas;
    }

    public void setVideoAulas(List<VideoAula> videoAulas) {
        this.videoAulas = videoAulas;
    }

    public List<MaterialApoio> getMaterialApoios() {
        return materialApoios;
    }

    public void setMaterialApoios(List<MaterialApoio> materialApoios) {
        this.materialApoios = materialApoios;
    }

    public UUID getNivelId() {
        return nivelId;
    }

    public void setNivelId(UUID nivelId) {
        this.nivelId = nivelId;
    }

    public Integer getCargaHorariaMinima() {
        return cargaHorariaMinima;
    }

    public void setCargaHorariaMinima(Integer cargaHorariaMinima) {
        this.cargaHorariaMinima = cargaHorariaMinima;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }
}
