package com.example.babelup.service;
import com.example.babelup.entities.Enum.EnumStatusProgresso;
import com.example.babelup.entities.comunicacao.AlertaEvasao;
import com.example.babelup.entities.progressoGamificacao.ProgressoAluno;
import com.example.babelup.entities.usuarios.Aluno;
import com.example.babelup.repository.pedagogicos.AlertaEvasaoRepository;
import com.example.babelup.repository.pedagogicos.ProgressoAlunoRepository;
import com.example.babelup.repository.usuarios.AlunoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MonitoramentoEvasaoService {

    private static final Logger logger = LoggerFactory.getLogger(MonitoramentoEvasaoService.class);

    private static final int DIAS_LIMITE_INATIVIDADE = 14;

    private final AlunoRepository alunoRepository;
    private final ProgressoAlunoRepository progressoAlunoRepository;
    private final AlertaEvasaoRepository alertaEvasaoRepository;

    public MonitoramentoEvasaoService(AlunoRepository alunoRepository,
                                      ProgressoAlunoRepository progressoAlunoRepository,
                                      AlertaEvasaoRepository alertaEvasaoRepository) {
        this.alunoRepository = alunoRepository;
        this.progressoAlunoRepository = progressoAlunoRepository;
        this.alertaEvasaoRepository = alertaEvasaoRepository;
    }


    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void executarVarreduraDiariaDeEvasao() {
        logger.info("Iniciando rotina de monitoramento de evasão de alunos...");


        List<Aluno> alunos = alunoRepository.findAll();

        for (Aluno aluno : alunos) {
            analisarRiscoDoAluno(aluno);
        }

        logger.info("Rotina de monitoramento finalizada com sucesso.");
    }

    private void analisarRiscoDoAluno(Aluno aluno) {

        boolean jaPossuiAlertaPendente = alertaEvasaoRepository.existsByAlunoIdAndVisualizadoFalse(aluno.getId());

        if (jaPossuiAlertaPendente) {
            return;
        }


        LocalDateTime dataLimite = LocalDateTime.now().minusDays(DIAS_LIMITE_INATIVIDADE);


        List<ProgressoAluno> progressosEstagnados = progressoAlunoRepository
                .findByAlunoIdAndStatusAndUltimoAcessoBefore(aluno.getId(), EnumStatusProgresso.EM_ANDAMENTO, dataLimite);

        if (!progressosEstagnados.isEmpty()) {

            String moduloNomes = progressosEstagnados.stream()
                    .map(p -> p.getModulo().getTitulo())
                    .reduce((m1, m2) -> m1 + ", " + m2)
                    .orElse("Módulos desconhecidos");

            String criteriosQuebrados = String.format(
                    "Risco de Evasão: Aluno inativo há mais de %d dias nos seguintes módulos em andamento: [%s].",
                    DIAS_LIMITE_INATIVIDADE, moduloNomes
            );

            AlertaEvasao novoAlerta = new AlertaEvasao();
            novoAlerta.setAluno(aluno);
            novoAlerta.setCriterios(criteriosQuebrados);
            novoAlerta.setVisualizado(false);

            alertaEvasaoRepository.save(novoAlerta);

            logger.info("Alerta de evasão gerado para o aluno ID: {}", aluno.getId());
        }
    }
}