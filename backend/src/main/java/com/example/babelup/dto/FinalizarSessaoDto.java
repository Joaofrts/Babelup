package com.example.babelup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FinalizarSessaoDto(
        @JsonProperty("gravacao_url")
        String gravacaoUrl
) {}
