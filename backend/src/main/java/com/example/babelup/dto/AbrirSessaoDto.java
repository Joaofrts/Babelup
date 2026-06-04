package com.example.babelup.dto;

import com.example.babelup.entities.enumEntities.EnumModalidadeSessao;
import com.example.babelup.entities.enumEntities.EnumTipoSessao;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public record AbrirSessaoDto(
        @JsonProperty("professor_id")
        UUID professorId,
        @JsonProperty("modulo_id")
        UUID moduloId,
        @JsonProperty("tipo_sessao")
        EnumTipoSessao tipoSessao,
        @JsonProperty("modalidade")
        EnumModalidadeSessao modalidade,
        @JsonProperty("data_hora")
        LocalDateTime dataHora,
        @JsonProperty("max_alunos")
        Integer maxAlunos
) {}
