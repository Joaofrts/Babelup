package com.example.babelup.factory;

import com.example.babelup.dto.NovoUsuarioDto;
import com.example.babelup.entities.enumEntities.EnumPerfil;
import com.example.babelup.entities.usuarios.Usuario;
import com.example.babelup.exceptions.PerfilUsuarioInvalidoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UsuarioFactory {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioFactory.class);

    private final PasswordEncoder passwordEncoder;
    private final UsuarioValidator validator;
    private final Map<EnumPerfil, UsuarioCreationStrategy> strategies;

    public UsuarioFactory(
            PasswordEncoder passwordEncoder,
            UsuarioValidator validator,
            AlunoCreationStrategy alunoStrategy,
            ProfessorCreationStrategy professorStrategy,
            AdminCreationStrategy adminStrategy
    ) {
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
        this.strategies = Map.of(
                EnumPerfil.ALUNO, alunoStrategy,
                EnumPerfil.PROFESSOR, professorStrategy,
                EnumPerfil.ADMIN, adminStrategy
        );
        logger.debug("UsuarioFactory inicializado com {} estratégias", strategies.size());
    }

    public Usuario fabricar(NovoUsuarioDto dto) {
        logger.debug("Iniciando criação de usuário com perfil: {}", dto.perfil());

        validator.validarDTO(dto);
        logger.debug("DTO validado com sucesso");

        String senhaCriptografada = passwordEncoder.encode(dto.senha());
        logger.debug("Senha criptografada com sucesso");

        UsuarioCreationStrategy strategy = strategies.get(dto.perfil());

        if (strategy == null) {
            String mensagem = "Perfil desconhecido: " + dto.perfil();
            logger.error(mensagem);
            throw new PerfilUsuarioInvalidoException(mensagem);
        }

        logger.debug("Strategy encontrada para perfil: {}", dto.perfil());

        Usuario usuarioCriado = strategy.criar(dto, senhaCriptografada);

        logger.info("Usuário criado com sucesso: {} ({})", usuarioCriado.getEmail(), dto.perfil());
        return usuarioCriado;
    }
}



