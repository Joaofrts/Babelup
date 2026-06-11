package com.example.babelup.dto;

import com.example.babelup.entities.enumEntities.EnumPerfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AdminCadastroDto(
        @NotBlank
        String nome,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String senha,
        String telefone
) {
    public NovoUsuarioDto toNovoUsuarioDto() {
        return new NovoUsuarioDto(nome, email, senha, EnumPerfil.ADMIN, telefone, true, false, null);
    }
}
