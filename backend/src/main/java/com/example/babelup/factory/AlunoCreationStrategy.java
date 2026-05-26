package com.example.babelup.factory;

import com.example.babelup.dto.NovoUsuarioDto;
import com.example.babelup.entities.usuarios.Aluno;
import com.example.babelup.entities.usuarios.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AlunoCreationStrategy implements UsuarioCreationStrategy{
    private static final Logger logger = LoggerFactory.getLogger(AlunoCreationStrategy.class);

    @Override
    public Usuario criar(NovoUsuarioDto dto, String senhaCriptografada) {
        logger.debug("Criando novo Aluno: {}", dto.email());

        validaAlunoDTO(dto);
        logger.debug("Validações de Aluno passaram");

        Aluno aluno = new Aluno(dto, senhaCriptografada);

        logger.info("Aluno criado com sucesso: email={}, menor={}",
                dto.email(), dto.menorIdade());

        return aluno;
    }

    private void validaAlunoDTO(NovoUsuarioDto dto) {
        logger.debug("Validando dados específicos de Aluno: {}", dto.email());


        if (dto.aceiteTermos() == null || !dto.aceiteTermos()) {
            logger.warn("Aluno {} não aceitou termos de uso", dto.email());
            throw new IllegalStateException(
                    "O aluno deve aceitar os termos de uso da plataforma para se cadastrar."
            );
        }

        if (dto.menorIdade() == null) {
            logger.warn("Aluno {} não informou se é menor de idade", dto.email());
            throw new IllegalStateException(
                    "É necessário informar se você é menor de idade."
            );
        }

        if (dto.menorIdade() &&
                (dto.dadosResponsaveis() == null ||
                        dto.dadosResponsaveis().trim().isEmpty())) {

            logger.warn("Aluno {} é menor mas não forneceu dados do responsável", dto.email());
            throw new IllegalStateException(
                    "Dados do responsável são obrigatórios para menores de idade. " +
                            "Este campo garante que o responsável seja notificado de eventos importantes."
            );
        }

        logger.debug("Todas as validações de Aluno passaram: {}", dto.email());
    }


}
