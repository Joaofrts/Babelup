package com.example.babelup.factory;

import com.example.babelup.dto.NovoUsuarioDto;
import com.example.babelup.entities.Enum.EnumPerfil;
import com.example.babelup.entities.usuarios.Administrador;
import com.example.babelup.entities.usuarios.Aluno;
import com.example.babelup.entities.usuarios.Professor;
import com.example.babelup.entities.usuarios.Usuario;
import com.example.babelup.exceptions.PerfilUsuarioInvalidoException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioFactory {

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
    }

    public Usuario fabricar(NovoUsuarioDto dto) {
        // Validações comuns
        validator.validarDTO(dto);

        String senhaCriptografada = passwordEncoder.encode(dto.senha());

        UsuarioCreationStrategy strategy = strategies.getOrDefault(
                dto.perfil(),
                () -> { throw new PerfilUsuarioInvalidoException(
                        "Perfil desconhecido: " + dto.perfil()
                ); }
        );

        return strategy.criar(dto, senhaCriptografada);
    }
}



