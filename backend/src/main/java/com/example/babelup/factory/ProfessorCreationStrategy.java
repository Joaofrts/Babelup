package com.example.babelup.factory;

import com.example.babelup.dto.NovoUsuarioDto;
import com.example.babelup.entities.usuarios.Professor;
import com.example.babelup.entities.usuarios.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ProfessorCreationStrategy implements UsuarioCreationStrategy {

    private static final Logger logger = LoggerFactory.getLogger(ProfessorCreationStrategy.class);

    @Override
    public Usuario criar(NovoUsuarioDto dto, String senhaCriptografada) {
        logger.debug("Criando novo Professor: {}", dto.email());

        Professor professor = new Professor(dto, senhaCriptografada);

        logger.info("Professor criado com sucesso: email={}", dto.email());

        return professor;
    }
}
