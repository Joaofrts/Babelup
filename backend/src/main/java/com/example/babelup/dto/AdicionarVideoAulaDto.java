package com.example.babelup.dto;

import com.example.babelup.entities.enumEntities.EnumTipoVideo;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public record AdicionarVideoAulaDto(
        @JsonProperty("modulo_id")
        UUID moduloId,
        String titulo,
        String url,
        Integer duracao,
        EnumTipoVideo tipo,
        @JsonProperty("data_transmissao")
        LocalDateTime dataTransmissao
) {}
