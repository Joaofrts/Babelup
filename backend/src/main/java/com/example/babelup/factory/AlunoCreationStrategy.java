package com.example.babelup.factory;

import com.example.babelup.dto.NovoUsuarioDto;
import com.example.babelup.entities.usuarios.Aluno;
import com.example.babelup.entities.usuarios.Usuario;

public class AlunoCreationStrategy implements UsuarioCreationStrategy{
    @Override
    public Usuario criar(NovoUsuarioDto dto, String senhaCriptografada){
        validaAlunoDTO(dto);
        return new Aluno(dto,senhaCriptografada);

    }

    private void validaAlunoDTO(NovoUsuarioDto dto) {
        if (dto.aceitouTermosLgpd() == null || !dto.aceitouTermosLgpd()) {
            throw new IllegalStateException("O aluno deve aceitar os termos da LGPD para se cadastrar.");
        }
    }

}
