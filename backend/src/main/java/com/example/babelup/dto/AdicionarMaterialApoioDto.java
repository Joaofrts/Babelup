package com.example.babelup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record AdicionarMaterialApoioDto(
        @JsonProperty("modulo_id")
        UUID moduloId,
        String titulo,
        @JsonProperty("url_pdf")
        String urlPdf
) {}
