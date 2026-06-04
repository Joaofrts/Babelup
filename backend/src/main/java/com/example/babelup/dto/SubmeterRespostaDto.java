package com.example.babelup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record SubmeterRespostaDto(
        @JsonProperty("aluno_id")
        UUID alunoId,
        @JsonProperty("exercicio_id")
        UUID exercicioId,
        String resposta
) {}
