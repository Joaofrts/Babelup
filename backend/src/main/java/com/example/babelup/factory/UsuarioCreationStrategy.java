package com.example.babelup.factory;

import com.example.babelup.dto.NovoUsuarioDto;
import com.example.babelup.entities.usuarios.Usuario;

public interface UsuarioCreationStrategy {
    Usuario criar(NovoUsuarioDto dto, String senhaCriptografada);
}
