package com.example.babelup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record AdicionarPontosDto(
        @JsonProperty("aluno_id")
        UUID alunoId,
        Integer pontos
) {}
