package com.example.babelup.dto;

import com.example.babelup.entities.avaliacao.SessaoConversacao;
import com.example.babelup.entities.avaliacao.TesteDiagnostico;
import com.example.babelup.entities.pratica.Exercicio;
import com.example.babelup.entities.pratica.MaterialApoio;
import com.example.babelup.entities.pratica.VideoAula;
import com.example.babelup.entities.progressoGamificacao.Ranking;
import com.example.babelup.entities.progressoGamificacao.RespostaAluno;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class ApiResponseDtos {
    private ApiResponseDtos() {}

    public record VideoAulaResponse(UUID id, String titulo, String url, Integer duracao, String tipo,
                                    @JsonProperty("modulo_id") UUID moduloId) {
        public VideoAulaResponse(VideoAula aula) {
            this(aula.getId(), aula.getTitulo(), aula.getUrl(), aula.getDuracao(), aula.getTipo().name(),
                    aula.getModulo().getId());
        }
    }

    public record MaterialApoioResponse(UUID id, String titulo, @JsonProperty("url_pdf") String urlPdf,
                                        @JsonProperty("modulo_id") UUID moduloId) {
        public MaterialApoioResponse(MaterialApoio material) {
            this(material.getId(), material.getTitulo(), material.getUrlPdf(), material.getModulo().getId());
        }
    }

    public record ExercicioResponse(UUID id, String enunciado, String alternativas,
                                    @JsonProperty("videoaula_id") UUID videoAulaId) {
        public ExercicioResponse(Exercicio exercicio) {
            this(exercicio.getId(), exercicio.getEnunciado(), exercicio.getAlternativas(),
                    exercicio.getVideoAula().getId());
        }
    }

    public record RespostaAlunoResponse(UUID id, @JsonProperty("aluno_id") UUID alunoId,
                                        @JsonProperty("exercicio_id") UUID exercicioId,
                                        String resposta, Boolean correto, Integer tentativa) {
        public RespostaAlunoResponse(RespostaAluno respostaAluno) {
            this(respostaAluno.getId(), respostaAluno.getAluno().getId(), respostaAluno.getExercicio().getId(),
                    respostaAluno.getResposta(), respostaAluno.isCorreto(), respostaAluno.getTentativa());
        }
    }

    public record SessaoConversacaoResponse(UUID id, @JsonProperty("data_hora") LocalDateTime dataHora,
                                            @JsonProperty("professor_id") UUID professorId,
                                            @JsonProperty("modulo_id") UUID moduloId,
                                            @JsonProperty("aluno_ids") List<UUID> alunoIds,
                                            @JsonProperty("qtd_alunos") Integer qtdAlunos,
                                            @JsonProperty("max_alunos") Integer maxAlunos,
                                            @JsonProperty("tipo_sessao") String tipoSessao,
                                            String modalidade,
                                            String status,
                                            @JsonProperty("gravacao_url") String gravacaoUrl) {
        public SessaoConversacaoResponse(SessaoConversacao sessao) {
            this(sessao.getId(), sessao.getDataHora(), sessao.getProfessor().getId(),
                    sessao.getModulo() == null ? null : sessao.getModulo().getId(),
                    sessao.getAlunos().stream().map(a -> a.getId()).toList(),
                    sessao.getAlunos().size(), sessao.getMaxAlunos(), sessao.getTipoSessao().name(),
                    sessao.getModalidadeSessao().name(), sessao.getStatus().name(), sessao.getGravacaoUrl());
        }
    }

    public record AvaliacaoResponse(UUID id, @JsonProperty("sessao_id") UUID sessaoId,
                                    @JsonProperty("aluno_id") UUID alunoId,
                                    @JsonProperty("nivel_recomendado_id") UUID nivelId,
                                    @JsonProperty("tipo_teste") String tipoTeste,
                                    Double nota,
                                    @JsonProperty("parecer_professor") String parecerProfessor) {
        public AvaliacaoResponse(TesteDiagnostico avaliacao) {
            this(avaliacao.getId(), avaliacao.getSessaoConversacao().getId(), avaliacao.getAluno().getId(),
                    avaliacao.getNivel() == null ? null : avaliacao.getNivel().getId(),
                    avaliacao.getTipo().name(), avaliacao.getResultado(), avaliacao.getParecerProfessor());
        }
    }

    public record RankingResponse(UUID id, @JsonProperty("aluno_id") UUID alunoId, String aluno,
                                  Integer mes, Integer ano, Integer posicao, Integer pontuacao) {
        public RankingResponse(Ranking ranking) {
            this(ranking.getId(), ranking.getAluno().getId(), ranking.getAluno().getNome(),
                    ranking.getMes(), ranking.getAno(), ranking.getPosicao(), ranking.getPontuacao());
        }
    }
}
