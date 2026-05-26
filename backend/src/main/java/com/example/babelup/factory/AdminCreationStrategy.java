package com.example.babelup.factory;

import com.example.babelup.dto.NovoUsuarioDto;
import com.example.babelup.entities.usuarios.Administrador;
import com.example.babelup.entities.usuarios.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AdminCreationStrategy implements UsuarioCreationStrategy {

    private static final Logger logger = LoggerFactory.getLogger(AdminCreationStrategy.class);

    @Override
    public Usuario criar(NovoUsuarioDto dto, String senhaCriptografada) {
        logger.warn("⚠️  OPERAÇÃO DE SEGURANÇA: Criando novo ADMINISTRADOR - Email: {}", dto.email());

        Administrador admin = new Administrador(dto, senhaCriptografada);

        logger.warn("✓ ADMINISTRADOR CRIADO COM SUCESSO: email={}, nível={}",
                dto.email(), admin.getNivelAcesso());

        return admin;
    }
}
