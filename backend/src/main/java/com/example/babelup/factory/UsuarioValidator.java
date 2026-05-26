package com.example.babelup.factory;

import com.example.babelup.dto.NovoUsuarioDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UsuarioValidator {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioValidator.class);

    private static final int SENHA_MINIMA_LENGTH = 8;

    public void validarDTO(NovoUsuarioDto dto){
        if (dto==null){
            logger.warn("Tentativa de validar DTO nulo");
            throw new IllegalArgumentException("DTO de usuário não pode ser nulo");
        }

        if(dto.email() == null || dto.email().trim().isEmpty()){
            logger.warn("Email vazio ou nulo");
            throw new IllegalArgumentException("Email é obrigatório");
        }

        if (!isEmailValido(dto.email())) {
            logger.warn("Email inválido: {}", dto.email());
            throw new IllegalArgumentException("Email inválido: " + dto.email());
        }

        // Valida Nome
        if (dto.nome() == null || dto.nome().trim().isEmpty()) {
            logger.warn("Nome vazio ou null");
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        // Valida Senha
        if (dto.senha() == null || dto.senha().isEmpty()) {
            logger.warn("Senha vazia ou null");
            throw new IllegalArgumentException("Senha é obrigatória");
        }

        if (dto.senha().length() < SENHA_MINIMA_LENGTH) {
            logger.warn("Senha muito curta: {} caracteres", dto.senha().length());
            throw new IllegalArgumentException(
                    "Senha deve ter no mínimo " + SENHA_MINIMA_LENGTH + " caracteres"
            );
        }

        // Valida Perfil
        if (dto.perfil() == null) {
            logger.warn("Perfil null");
            throw new IllegalArgumentException("Perfil é obrigatório");
        }

        logger.debug("DTO validado com sucesso: {}", dto.email());
    }

    private boolean isEmailValido(String email) {
        // Verifica se tem @ e tem ponto depois
        return email.contains("@") &&
                email.contains(".") &&
                email.indexOf("@") < email.lastIndexOf(".");
    }
}
