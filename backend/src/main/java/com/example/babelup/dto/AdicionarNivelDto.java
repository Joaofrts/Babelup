package com.example.babelup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.UUID;

public record AdicionarNivelDto(
        @JsonProperty("idioma")
        String idioma,
        @JsonProperty("nome")
        String nome,
        @JsonProperty("ordem")
        Integer ordem,
        @JsonProperty("carga_horaria")
        Integer cargaHoraria,
        @JsonProperty("descricao")
        String descricao,
        @JsonProperty("preco_mensal")
        BigDecimal precoMensal) {}
