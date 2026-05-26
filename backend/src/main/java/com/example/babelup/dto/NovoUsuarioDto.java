package com.example.babelup.dto;

import com.example.babelup.entities.Enum.EnumPerfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record NovoUsuarioDto(
        @NotNull
        String nome,
        @NotNull
        @Email
        String email,
        @NotNull
                             String senha,
                             EnumPerfil perfil,
                             String telefone,
                             Boolean aceitouTermosLgpd,
                             Boolean menorIdade,
                             String dadosResponsaveis
){}
