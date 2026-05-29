package com.example.babelup.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CursoCatalogoDTO(
        UUID id,
        String titulo,
        String descricao,
        BigDecimal precoMensal

) {}