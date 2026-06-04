package com.example.babelup.dto;

import com.example.babelup.entities.enumEntities.EnumTipoTeste;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record AvaliacaoNivelamentoDto(
        @JsonProperty("sessao_id")
        UUID sessaoId,
        @JsonProperty("aluno_id")
        UUID alunoId,
        @JsonProperty("nivel_recomendado_id")
        UUID nivelRecomendadoId,
        @JsonProperty("tipo_teste")
        EnumTipoTeste tipoTeste,
        Double nota,
        @JsonProperty("parecer_professor")
        String parecerProfessor
) {}
