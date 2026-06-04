package com.example.babelup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record AdicionarExercicioDto(
        @JsonProperty("videoaula_id")
        UUID videoAulaId,
        String enunciado,
        List<String> alternativas,
        @JsonProperty("resposta_correta")
        String respostaCorreta
) {}
